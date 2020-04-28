package com.cheng.inputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class WholeRecordReader extends RecordReader<Text, BytesWritable> {
    Text key = new Text();
    BytesWritable value = new BytesWritable();
    boolean isProcessed = true;
    FileSplit split;
    Configuration conf;
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        this.split = (FileSplit)split;
        this.conf = context.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        // getLength返回的是split的字节数
            // 0 初始化缓存
        int length = (int)split.getLength();
        byte[] buf = new byte[length];

        if(isProcessed){
            // 1 获取fs对象
            Path path = split.getPath();
            FileSystem fs = path.getFileSystem(conf);
            FSDataInputStream fis = null;
            try {
                // 2 获取输入流
                fis = fs.open(path);

                // 3 拷贝
                IOUtils.readFully(fis, buf, 0, length);

                // 4 封装v
                value.set(buf, 0, length);

                // 5 封装k
                key.set(path.toString());


            } catch (Exception e){

            } finally {
                // 6 关闭资源
                IOUtils.closeStream(fis);
            }



            isProcessed = false;

            return true;
        }
        return false;
    }

    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}

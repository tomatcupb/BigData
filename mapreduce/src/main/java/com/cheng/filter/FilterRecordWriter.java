package com.cheng.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class FilterRecordWriter extends RecordWriter<Text, NullWritable> {
    FSDataOutputStream fosTarget;
    FSDataOutputStream fosOther;
    public FilterRecordWriter(TaskAttemptContext job) throws IOException {
        //1. 获取文件系统
        Configuration conf = job.getConfiguration();
        try {
            //1. 获取文件系统
            FileSystem fs = FileSystem.get(conf);

            //2. 创建文件路径
            Path pathTarget = new Path("C:/Users/SirAlex/Desktop/outputFilter/targat.log");
            Path pathOther = new Path("C:/Users/SirAlex/Desktop/outputFilter/other.log");

            //3. 创建输出流
            fosTarget = fs.create(pathTarget);
            fosOther = fs.create(pathOther);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void write(Text key, NullWritable value) throws IOException, InterruptedException {
        String str = key.toString();
        if(str.contains("baidu")){
            fosTarget.write(str.getBytes());
        } else {
            fosOther.write(str.getBytes());
        }
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        if(fosTarget!=null){
            fosTarget.close();
        }
        if(fosOther!=null){
            fosOther.close();
        }
    }
}

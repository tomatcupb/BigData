package com.cheng.inputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

public class WholeDriver {
    /**
     * 两种驱动方法：
     * 1.自定义reduce类
     * 2.但是因为reducer是简单的合并操作。没有其他的逻辑，
     *  可以不写reduce类，会自动有reduce进行文件的合并：不写map输出阶段的参数
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        //1. 获取job对象信息
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置加载jar包
        job.setJarByClass(WholeDriver.class);

        //3. 设置Mapper和Reducer的class类
        job.setMapperClass(WholeMapper.class);
//        job.setReducerClass(WholeReducer.class);

        //4. 设置Mapper输出的KV类型
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(BytesWritable.class);

        //5. 设置Reducer输出的KV类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        //6. 设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        Path outDir = new Path(args[1]);
        FileSystem fs = outDir.getFileSystem(job.getConfiguration());
        if (fs.exists(outDir)){
            fs.delete(outDir, true);
        }
        FileOutputFormat.setOutputPath(job, outDir);

        //7. 设置读取格式
        job.setInputFormatClass(WholeFileInputFormat.class);

        //8. 设置输出的outputFormat
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //9. 提交
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }
}

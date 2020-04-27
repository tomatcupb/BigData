package com.cheng.flow;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取job对象信息
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置加载jar包的位置Set the Jar by finding where a given class came from.
        job.setJarByClass(FlowDriver.class);

        //3. 设置Mapper和Reducer的class类
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        //4. 设置Mapper输出的KV类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        //5. 设置Reducer输出的KV类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //6. 自定义分区
        job.setPartitionerClass(FlowPartitioner.class);
        job.setNumReduceTasks(1);

        //7. 设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        Path outDir = new Path(args[1]);
        FileSystem fs = outDir.getFileSystem(job.getConfiguration());
        if (fs.exists(outDir)){
            fs.delete(outDir, true);
        }
        FileOutputFormat.setOutputPath(job, outDir);

        //7. 提交
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }
}

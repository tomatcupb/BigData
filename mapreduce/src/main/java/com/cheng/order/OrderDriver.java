package com.cheng.order;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class OrderDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取job对象信息
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置加载jar包的位置
        job.setJarByClass(OrderDriver.class);

        //3. 设置Mapper和Reducer的class类
        job.setMapperClass(OrderMapper.class);
        job.setReducerClass(OrderReducer.class);

        //4. 设置Mapper输出的KV类型
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        //5. 设置Reducer输出的KV类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        //6. 设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        Path outDir = new Path(args[1]);
        FileSystem fs = outDir.getFileSystem(job.getConfiguration());
        if (fs.exists(outDir)){
            fs.delete(outDir, true);
        }
        FileOutputFormat.setOutputPath(job, outDir);

        //7. 设置分区
        job.setPartitionerClass(OrderPartitioner.class);
        job.setNumReduceTasks(3);

        // 8. 设置groupingComparator:设置reduce端的分组
        job.setGroupingComparatorClass(OrderGroupingComparator.class);

        //7. 提交
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }
}
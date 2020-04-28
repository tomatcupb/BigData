package com.cheng.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

public class FilterDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        //1. 获取job对象信息
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 设置加载jar包的位置
        job.setJarByClass(FilterDriver.class);

        //3. 设置Mapper和Reducer的class类
        job.setMapperClass(FilterMapper.class);
        job.setReducerClass(FilterReducer.class);

        //4. 设置Mapper输出的KV类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        //5. 设置outputFormat
        job.setOutputFormatClass(FilterOutputFormat.class);

        //6. 设置Reducer输出的KV类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        //7. 设置输入输出路径
        //此处的校验文件将会存放校验文件_SUCCESS
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        Path outDir = new Path(args[1]);
        FileSystem fs = outDir.getFileSystem(job.getConfiguration());
        if (fs.exists(outDir)){
            fs.delete(outDir, true);
        }
        FileOutputFormat.setOutputPath(job, outDir);

        //8. 提交
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }
}

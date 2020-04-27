package com.cheng.flowSort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowSortMapper extends Mapper<LongWritable, Text, FlowBeanSort, Text> {
    FlowBeanSort bean = new FlowBeanSort();
    Text phoneNum = new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        phoneNum.set(fields[0]);
        bean.set(Long.parseLong(fields[1]),Long.parseLong(fields[2]));
        context.write(bean, phoneNum);
    }
}

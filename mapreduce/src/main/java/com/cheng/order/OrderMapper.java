package com.cheng.order;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class OrderMapper extends Mapper<LongWritable, Text, OrderBean, DoubleWritable> {
    OrderBean bean = new OrderBean();
    DoubleWritable price = new DoubleWritable();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        bean.set(fields[0],Double.parseDouble(fields[2]));
        price.set(Double.parseDouble(fields[2]));
        context.write(bean, price);
    }
}

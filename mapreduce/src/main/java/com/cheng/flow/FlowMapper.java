package com.cheng.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
    Text phone = new Text();
    FlowBean bean = new FlowBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        long upFlow = Long.parseLong(fields[fields.length-3]);
        long downFlow = Long.parseLong(fields[fields.length-2]);
        phone.set(fields[1]);
        bean.set(upFlow, downFlow);
        context.write(phone, bean);
    }
}

package com.cheng.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
    FlowBean valOut = new FlowBean();
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long sumUp = 0;
        long sumDown = 0;
        for (FlowBean value : values) {
            sumUp += value.getUpFlow();
            sumDown += value.getDownFlow();
        }
        valOut.set(sumUp,sumDown);
        System.out.println(valOut);
        context.write(key, valOut);
    }
}

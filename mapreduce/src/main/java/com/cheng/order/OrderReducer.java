package com.cheng.order;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class OrderReducer extends Reducer<OrderBean, DoubleWritable, Text, DoubleWritable> {
    Text keyOut = new Text();
    @Override
    protected void reduce(OrderBean key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        DoubleWritable value = values.iterator().next();
        String orderNum = key.getOrderNum();
        keyOut.set(orderNum);
        context.write(keyOut, value);
    }
}

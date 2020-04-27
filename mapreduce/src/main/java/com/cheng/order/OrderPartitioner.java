package com.cheng.order;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class OrderPartitioner extends Partitioner<OrderBean, DoubleWritable> {
    @Override
    public int getPartition(OrderBean orderBean, DoubleWritable doubleWritable, int numPartitions) {
        int res = 0;
        if (orderBean.getOrderNum().endsWith("1")){
            res = 1;
        } else if(orderBean.getOrderNum().endsWith("2")){
            res = 2;
        }
        return res;
    }
}

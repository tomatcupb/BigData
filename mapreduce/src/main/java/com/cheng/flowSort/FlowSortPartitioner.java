package com.cheng.flowSort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FlowSortPartitioner extends Partitioner<FlowBeanSort, Text> {
    @Override
    public int getPartition(FlowBeanSort flowBeanSort, Text text, int numPartitions) {
        long upFlow = flowBeanSort.getUpFlow();
        return upFlow%2==1?0:1;
    }
}

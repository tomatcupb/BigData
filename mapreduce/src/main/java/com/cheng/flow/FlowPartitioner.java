package com.cheng.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FlowPartitioner extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
        String num = text.toString().substring(0, 3);
        int res = 0;
        if(num.equals("135")){
            res = 1;
        }else if(num.equals("136")){
            res = 2;
        }else if(num.equals("137")){
            res = 3;
        }else if(num.equals("138")){
            res = 4;
        }
        return res;
    }
}

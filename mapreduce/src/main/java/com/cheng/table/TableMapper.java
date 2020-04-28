package com.cheng.table;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class TableMapper extends Mapper<LongWritable, Text, Text, TableBean> {
    TableBean tableBean = new TableBean();
    Text keyOut = new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 此处增加了一个通过文件名区分table的flag过程
        // 同时在bean中需要添加这个flag属性
        FileSplit inputSplit = (FileSplit)context.getInputSplit();
        String name = inputSplit.getPath().getName();
        String[] fields = value.toString().split("\t");
        if(name.startsWith("order")){
            tableBean.setId(Integer.parseInt(fields[0]));
            tableBean.setPid(Integer.parseInt(fields[1]));
            tableBean.setAmount(Integer.parseInt(fields[2]));
            tableBean.setPname("");
            tableBean.setFlag(1);
            keyOut.set(fields[1]);
        } else {
            tableBean.setId(Integer.parseInt(fields[0]));
            tableBean.setPid(0);
            tableBean.setAmount(0);
            tableBean.setPname(fields[1]);
            tableBean.setFlag(2);
            keyOut.set(fields[0]);
        }
        context.write(keyOut, tableBean);
    }
}

package com.cheng.table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;

public class TableReducer extends Reducer<Text, TableBean, TableBean, NullWritable> {
    @Override
    protected void reduce(Text key, Iterable<TableBean> values, Context context) throws IOException, InterruptedException {
        ArrayList<TableBean> list = new ArrayList<>();
        String pname = "";
        TableBean newBean = new TableBean();
        for (TableBean bean: values){
            int flag = bean.getFlag();
            if(flag==1){
                try {
                    BeanUtils.copyProperties(newBean, bean);
                    list.add(newBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                pname = bean.getPname();
            }
        }
        for (TableBean bean :list){
            bean.setPname(pname);
            context.write(bean, NullWritable.get());
        }
    }
}

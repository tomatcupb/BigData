package com.cheng.distributedCahe;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DCMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    HashMap<String, String> map = new HashMap<>();
    @Override
    protected void setup(Context context) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("pd.txt")));
        String line = reader.readLine();
        while (line!=null){
            String[] split = line.split("\t");
            map.put(split[0],split[1]);
            line = reader.readLine();
        }
        reader.close();
    }

    Text text = new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        String id = fields[0];
        String pname = map.get(fields[1]);
        String amout = fields[2];
        String out = id+"\t"+pname+"\t"+amout;
        text.set(out);
        context.write(text, NullWritable.get());
    }
}

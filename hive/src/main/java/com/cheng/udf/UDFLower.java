package com.cheng.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class UDFLower extends UDF {
    public String evaluate (final String s){
        if (s == null) {
            return null;
        }
        return s+Math.random()*10;
    }
}

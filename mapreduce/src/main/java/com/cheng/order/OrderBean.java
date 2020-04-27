package com.cheng.order;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OrderBean implements WritableComparable<OrderBean> {
    private String orderNum;
    private double price;

    public OrderBean() {
    }

    public OrderBean(String orderNum, double price) {
        this.orderNum = orderNum;
        this.price = price;
    }

    public void set(String orderNum, double price){
        this.orderNum = orderNum;
        this.price = price;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return  orderNum + "\t" +price;
    }

    @Override
    public int compareTo(OrderBean o) {
        int res = this.orderNum.compareTo(o.getOrderNum());
        if(res==0){
            res = this.price>o.price?-1:1;
        }
        return res;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(orderNum);
        out.writeDouble(price);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.orderNum = in.readUTF();
        this.price = in.readDouble();
    }
}

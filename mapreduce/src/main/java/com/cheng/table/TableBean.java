package com.cheng.table;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TableBean implements Writable {
    private int id;
    private int pid;
    private int amount;
    private String pname;
    private int flag;//表的标记

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(id);
        out.writeInt(pid);
        out.writeInt(amount);
        out.writeUTF(pname);
        out.writeInt(flag);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.pid = in.readInt();
        this.amount = in.readInt();
        this.pname = in.readUTF();
        this.flag = in.readInt();
    }

    @Override
    public String toString() {
        return id+"\t"+pid+"\t"+pname+"\t"+amount;
    }
}

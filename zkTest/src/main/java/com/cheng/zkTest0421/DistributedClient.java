package com.cheng.zkTest0421;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DistributedClient {
    public static void main(String[] args) throws Exception {
        DistributedClient client = new DistributedClient();

//        1. 连接Zookeeper server
        client.getconn();

//        2. 监听节点
        client.getChildren();

//        3. 业务逻辑
        client.process();
    }

    private void process() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zkCli.getChildren("/servers", true);
        ArrayList<String> list = new ArrayList<String>();
        for(String child : children){
            list.add(new String(zkCli.getData("/servers/"+child, false, null)));
        }
        System.out.println(list);
    }

    String connectString = "nn1.hadoop:2181,nn2.hadoop:2181,s1.hadoop:2181";
    int sessionTimeout = 2000;
    ZooKeeper zkCli;
    private void getconn() throws IOException {
        zkCli = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                try {
                    getChildren();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

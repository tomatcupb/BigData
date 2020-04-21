package com.cheng.zkTest0421;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ZookeeperTest {

    String connectString = "nn1.hadoop:2181,nn2.hadoop:2181,s1.hadoop:2181";
    int sessionTimeout = 2000;
    ZooKeeper zkCli;

    @Before
    public void init() throws IOException {
        zkCli = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                try {
                    List<String> children = zkCli.getChildren("/testAPI", false);
                    for(String s: children){
                        System.out.println(s);
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Test
    public void createNode() throws KeeperException, InterruptedException {
        String s = zkCli.create("/testAPI", "this is a test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);
//        getNodeAndWatch();
    }

    @Test
    public void getNodeAndWatch() throws KeeperException, InterruptedException {
        List<String> children = zkCli.getChildren("/testAPI", true);
//        for (String child : children) {
//            System.out.println(child);
//        }
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void exist() throws KeeperException, InterruptedException{
        Stat stat = zkCli.exists("/testAPI", false);
        System.out.println(stat==null? "not exist":"exist");
    }
}

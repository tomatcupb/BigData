package com.cheng.zkTest0421;
import org.apache.zookeeper.*;
import java.io.IOException;

public class DistributeSever {
    public static void main(String[] args) throws Exception {
        DistributeSever sever = new DistributeSever();
//    1. 连接到Zookeeper Server
        sever.getConn();

//    2. 注册节点
        sever.register(args[0]);

//    3. process
        sever.process();
    }


    String connectString = "nn1.hadoop:2181,nn2.hadoop:2181,s1.hadoop:2181";
    int sessionTimeout = 2000;
    ZooKeeper zkCli;

    private void process() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void register(String hostname) throws KeeperException, InterruptedException {
        zkCli.create("/servers/", hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname +"is online ");
    }

    private void getConn() throws IOException {
        zkCli = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
//                try {
//                    List<String> children = zkCli.getChildren("/testAPI", false);
//                    for(String s: children){
//                        System.out.println(s);
//                    }
//                } catch (KeeperException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }


}

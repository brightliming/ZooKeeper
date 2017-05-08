package com.config;

import org.apache.zookeeper.*;

/**
 * Created by limingliang on 2017/5/8.
 */
public class SetConfig {
    public static String url = "192.168.0.4:2181";

    private final static String root = "/myConfig";
    private final static String UrlNode = root + "/url";
    private final static String userNameNode = root + "/username";
    private final static String passWdNode = root + "/passwd";

    private final static String auth_type = "digest";
    private final static String auth_passwd = "password";

    public static void main(String[] args) throws Exception{

        ZooKeeper zk = new ZooKeeper(url, 3000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("触发了事件："+event.getType());
            }
        });

        while(ZooKeeper.States.CONNECTED != zk.getState()){
            Thread.sleep(3000);
        }

        zk.addAuthInfo(auth_type,auth_passwd.getBytes());

        if(zk.exists(root,true)==null){
            zk.create(root,"root".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        }

        if(zk.exists(UrlNode,true)==null){
            zk.create(UrlNode,"192.168.0.1:2181".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        }

        if(zk.exists(userNameNode,true)==null){
            zk.create(userNameNode,"admin".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        }

        if(zk.exists(passWdNode,true)==null){
            zk.create(passWdNode,"admin123".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        }
        zk.close();

    }
}

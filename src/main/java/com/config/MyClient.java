package com.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by limingliang on 2017/5/8.
 */
public class MyClient implements Watcher{

    public static String url = "192.168.0.4:2181";

    public static String root = "/myConfig";
    public String UrlNode = root + "/url";
    public String userNameNode = root + "/username";
    public String passWdNode = root + "/passwd";

    public static String auth_type = "digest";
    public static String auth_passwd = "password";

    public String getUrlNode() {
        return UrlNode;
    }

    public String getUrlString() {
        return urlString;
    }

    public String getUserName() {
        return userName;
    }
    public String getPassWd() {
        return passWd;
    }

    private String urlString;
    private String userName;
    private String passWd;

    ZooKeeper zk = null;


    public MyClient() throws Exception{
        getZk();
        initValue();
    }
    public void initValue(){
        try {
            urlString = new String(zk.getData(UrlNode,true,null));
            userName = new String(zk.getData(userNameNode,true,null));
            passWd = new String(zk.getData(passWdNode,true,null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ZooKeeper getZk() throws Exception {
        zk = new ZooKeeper(url,3000,this);
        zk.addAuthInfo(auth_type,auth_passwd.getBytes());
        while(zk.getState() != ZooKeeper.States.CONNECTED){
            Thread.sleep(3000);
        }
        return zk;
    }

    public static void main(String[] args) throws Exception{
         MyClient zkTest = new MyClient();
        while(true){
            System.out.println(zkTest.getUrlString());
            System.out.println(zkTest.getUserName());
            System.out.println(zkTest.getPassWd());
            System.out.println("==========================");
            Thread.sleep(5000);
        }


    }

    public void process(WatchedEvent event) {
        if(event.getType() == Watcher.Event.EventType.None){
            System.out.println("连接服务器成功");
        }else if(event.getType() == Event.EventType.NodeCreated){
            System.out.println("节点创建成功");
        }else if(event.getType() == Event.EventType.NodeChildrenChanged){
            System.out.println("子节点更新成功");
            initValue();
        }else if(event.getType() == Event.EventType.NodeDataChanged){
            System.out.println("节点更新成功");
            initValue();
        }else if(event.getType() == Event.EventType.NodeDeleted){
            System.out.println("节点删除成功");
        }

    }


}

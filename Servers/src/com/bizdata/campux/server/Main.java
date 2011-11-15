package com.bizdata.campux.server;

import com.bizdata.campux.server.userprofile.ServerUserStatus;

/**
 * Start Servers
 * @author yuy
 */
public class Main {
    static public void main(String args[]) throws Exception{
        System.out.println("startServer");
        ServerUserStatus instance = new ServerUserStatus();
        instance.startServer();
    }
}

package com.bizdata.campux.server;

import com.bizdata.campux.server.userstatus.ServerUserStatus;
import java.io.FileInputStream;

/**
 * Start Servers
 * @author yuy
 */
public class Main {
    static public void main(String args[]) throws Exception{
        com.bizdata.campux.sdk.Config.init(new FileInputStream("sdk.config"));
        System.out.println("startServer");
        ServerUserStatus instance = new ServerUserStatus();
        instance.startServer();
    }
}

package com.bizdata.campux.server;

import com.bizdata.campux.server.userstatus.ServerUserStatus;
import com.bizdata.campux.server.wifilocator.ServerWifiLocator;
import java.io.FileInputStream;

/**
 * Start Servers
 * @author yuy
 */
public class Main {
    static public void main(String args[]) throws Exception{
        com.bizdata.campux.sdk.Config.init(new FileInputStream("sdk.config"));
        if(args.length==0){
            System.out.println("Please choose service to start:");
            System.out.println("    UserStatus");
            System.out.println("    Locator");
            return;
        }
        
        ServerBase instance = null;
        if( "userstatus".equalsIgnoreCase(args[0])){
            instance = new ServerUserStatus();
        }else if( "locator".equalsIgnoreCase(args[0])){
            instance = new ServerWifiLocator();
        }
        System.out.println("start server: " + instance.getName());
        instance.startServer();
    }
}

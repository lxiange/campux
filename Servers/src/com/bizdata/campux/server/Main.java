package com.bizdata.campux.server;

import com.bizdata.campux.server.bubble.ServerBubble;
import com.bizdata.campux.server.classroom.ServerClassroom;
import com.bizdata.campux.server.docanalysis.ServerDocAnalysis;
import com.bizdata.campux.server.friends.ServerFriends;
import com.bizdata.campux.server.info.ServerInfo;
import com.bizdata.campux.server.userinfo.ServerUserInfo;
import com.bizdata.campux.server.userstatus.ServerUserStatus;
import com.bizdata.campux.server.wifilocator.ServerWifiLocator;
import java.io.FileInputStream;

/**
 * Start Servers
 * @author yuy
 */
public class Main {
    static public void main(String args[]) throws Exception{
        // initialize the Config class
        com.bizdata.campux.sdk.Config.init(new FileInputStream("sdk.config"));
        // parse input to choose the server
        if(args.length==0){
            System.out.println("Please choose service to start:");
            System.out.println("    UserStatus");
            System.out.println("    Locator");
            System.out.println("    UserInfo");
            System.out.println("    DocAnalysis");
            System.out.println("    Bubble");
            System.out.println("    Friend");
            System.out.println("    Info");
            System.out.println("    ClassRoom");
            return;
        }
        ServerBase instance = null;
        if( "userstatus".equalsIgnoreCase(args[0])){
            instance = new ServerUserStatus();
        }else if( "locator".equalsIgnoreCase(args[0])){
            instance = new ServerWifiLocator();
        }else if( "userinfo".equalsIgnoreCase(args[0])){
            instance = new ServerUserInfo();
        }else if( "docanalysis".equalsIgnoreCase(args[0])){
            instance = new ServerDocAnalysis();
        }else if( "bubble".equalsIgnoreCase(args[0])){
            instance = new ServerBubble();
        }else if( "friend".equalsIgnoreCase(args[0])){
            instance = new ServerFriends();
        }else if( "info".equalsIgnoreCase(args[0])){
            instance = new ServerInfo();
        }else if( "classroom".equalsIgnoreCase(args[0])){
            instance = new ServerClassroom();
        }
        // start the server
        System.out.println("start server: " + instance.getServerName());
        instance.startServer();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.userinfo;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.CommonServer;
import com.bizdata.campux.server.ServerBase;
import com.bizdata.campux.server.exception.ParseEndException;
import com.bizdata.campux.server.userstatus.SAXHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author yuy
 */
public class ServerUserInfo extends ServerBase {
   public ServerUserInfo(){
    	 m_serverName = "ServerUserInfo";
    	 m_port = Integer.parseInt(Config.getValue("ServicePort_UserInfo"));
         System.out.println(m_port);
    	 m_saxhandlerClass = SAXHandler.class;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.info;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.ServerBase;

/**
 *
 * @author yuy
 * @date 2012-02-19 05:30:11
 */
public class ServerInfo extends ServerBase{
    public ServerInfo(){
        m_serverName = "ServerInfo";
        m_port = Integer.parseInt(Config.getValue("ServicePort_UserInfo"));
        m_saxhandlerClass = SAXHandler.class;
    }
}

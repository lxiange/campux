/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.friends;

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.server.ServerBase;

/**
 *
 * @author yuy
 * @date 2012-02-12 10:12:20
 */
public class ServerFriends extends ServerBase{
    public ServerFriends(){
        m_serverName = "ServerFriends";
        m_port = Integer.parseInt(Config.getValue("ServicePort_Friend"));
        m_saxhandlerClass = SAXHandler.class;
    }
}

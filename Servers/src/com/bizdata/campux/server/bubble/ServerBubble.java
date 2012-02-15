/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.bubble;

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.server.ServerBase;

/**
 *
 * @author yuy
 * @date 2012-02-12 10:12:43
 */
public class ServerBubble extends ServerBase{
    public ServerBubble(){
        m_serverName = "ServerBubble";
        m_port = Integer.parseInt(Config.getValue("ServicePort_Bubble"));
        m_saxhandlerClass = SAXHandler.class;
    }
}
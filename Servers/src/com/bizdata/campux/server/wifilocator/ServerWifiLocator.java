/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.wifilocator;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.ServerBase;

/**
 *
 * @author yuy
 * @date 2011-12-21 03:46:54
 */
public class ServerWifiLocator extends ServerBase {
    public ServerWifiLocator(){
        m_serverName = "ServerWifiLocator";
        m_port = Integer.parseInt(Config.getValue("ServicePort_WifiLocator"));
        m_saxhandlerClass = SAXHandler.class;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.docanalysis;

import com.bizdata.campux.server.ServerBase;
import com.bizdata.campux.server.Config;

/**
 *
 * @author yuy
 * @date 2011-12-27 10:52:16
 */
public class ServerDocAnalysis extends ServerBase{
    public ServerDocAnalysis(){
        m_serverName = "ServerDocAnalysis";
        m_port = Integer.parseInt(Config.getValue("ServicePort_DocAnalysis"));
        m_saxhandlerClass = SAXHandler.class;
    }
}

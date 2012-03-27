/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.classroom;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.ServerBase;

/**
 *
 * @author yuy
 * @date 2012-02-19 05:29:53
 */
public class ServerClassroom extends ServerBase{
    public ServerClassroom(){
        m_serverName = "ServerClassroom";
        m_port = Integer.parseInt(Config.getValue("ServicePort_ClassSchedule"));
        m_saxhandlerClass = SAXHandler.class;
    }
}

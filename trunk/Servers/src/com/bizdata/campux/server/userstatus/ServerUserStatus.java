package com.bizdata.campux.server.userstatus;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.ServerBase;
/**
 * Define the UserStatus Server
 * @author yuy
 */
public class ServerUserStatus extends ServerBase {
    public ServerUserStatus(){
        m_serverName = "ServerUserStatus";
        m_port = Integer.parseInt(Config.getValue("ServicePort_UserStatus"));
        m_saxhandlerClass = SAXHandler.class;
    }
}

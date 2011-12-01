/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.userinfo;

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.sdk.Log;
import com.bizdata.campux.server.CommonServer;
import com.bizdata.campux.server.ServerBase;
import com.bizdata.campux.server.exception.ParseEndException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author yuy
 */
public class ServerUserInfo extends ServerBase {
    private static int s_port = -1;
    
    @Override
    public void startServer(){
        s_port = Integer.parseInt(Config.getValue("ServicePort_UserInfo"));
        m_commonserver = new CommonServer();
        m_commonserver.startServer(s_port, this);
    }
}

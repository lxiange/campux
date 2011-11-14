package com.bizdata.campux.server.userprofile;

import com.bizdata.campux.server.CommonServer;
import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.ServerBase;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.exception.ParseEndException;
import java.net.SocketException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
/**
 *
 * @author yuy
 */
public class ServerUserStatus extends ServerBase {
    private static int s_port = -1;
    
    @Override
    public void startServer(){
        s_port = Integer.parseInt(Config.getValue("ServicePort_UserStatus"));
        m_commonserver = new CommonServer();
        m_commonserver.startServer(s_port, this);
    }
    
    @Override
    public void run(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(m_inputstream, new SAXHandler(m_outputstream) );
        }catch(ParseEndException exc)
        {
            //the exception used to exit the parsing, ignore
        }
        catch(Exception exc){
            Log.log("ServerUserprofile", Log.Type.NOTICE, exc);
            return;
        }
    }
}

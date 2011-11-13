package com.bizdata.campux.server.userprofile;

import com.bizdata.campux.server.CommonServer;
import com.bizdata.campux.server.ServerBase;
import com.bizdata.campux.server.Log;
import java.io.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author yuy
 */
public class ServerUserprofile extends ServerBase {
    private static int s_port = 2505;
    
    @Override
    public void startServer(){
        m_commonserver = new CommonServer();
        m_commonserver.startServer(s_port, this);
    }
    
    @Override
    public void run(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(m_inputstream, new SAXHandler(m_outputstream) );
        }catch(Exception exc){
            Log.log("ServerUserprofile", Log.Type.NOTICE, exc);
            return;
        }
    }
}

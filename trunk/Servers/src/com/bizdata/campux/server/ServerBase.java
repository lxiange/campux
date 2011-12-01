package com.bizdata.campux.server;

import com.bizdata.campux.server.exception.ParseEndException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * HandlerBase implements common functions of a server handler
 * @author yuy
 */
public abstract class ServerBase extends Thread{
    // the name of this server. Subclass should overwrite this name
    protected String m_serverName = "ServerBase";
    // running TCP port
    protected int m_port = -1;
    // the input stream from network client
    protected InputStream m_inputstream = null;
    // the output stream to the network client
    protected OutputStream m_outputstream = null;
    // the server listening to the client
    protected CommonServer m_commonserver = new CommonServer();
    // SAX handler deals with client requests
    protected Class m_saxhandlerClass=null;
    
    /**
     * The CommonServer will call this interface when it gets requests
     * @param input input stream
     * @throws Exception 
     */
    public void handle(InputStream input, OutputStream output) throws Exception{
        ServerBase h = this.getClass().newInstance();
        h.m_inputstream = input;
        h.m_outputstream = output;
        h.m_saxhandlerClass = m_saxhandlerClass;
        h.start();
    }
    
    /**
     * parse the input
     */
    @Override
    public void run(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = factory.newSAXParser();
            // new an SAX handler from the class
            SAXHandlerBase saxhandler = (SAXHandlerBase)m_saxhandlerClass.newInstance();
            saxhandler.setOutputStream(m_outputstream);
            // parse input
            saxParser.parse(m_inputstream, saxhandler);
        }catch(ParseEndException exc)
        {
            //the exception used to exit the parsing, ignore
        }
        catch(Exception exc){
            Log.log(m_serverName, Log.Type.NOTICE, exc);
            return;
        }
    }
    
    /**
     * start running the server    
     */
    public void startServer(){
        m_commonserver.startServer(m_port, this);
    }
    
    /**
     * common stop process
     */
    public void stopServer(){
        m_commonserver.stopServer();
    }
}

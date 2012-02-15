package com.bizdata.campux.server;

import com.bizdata.campux.server.exception.ParseEndException;
import java.io.ByteArrayInputStream;
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
    
    //protected byte[] m_buffer ;= new byte[100*1024*1024];
    
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
            //InputStream input = bufferInputstream(m_inputstream);
            
            SAXParser saxParser = factory.newSAXParser();
            // new an SAX handler from the class
            SAXHandlerBase saxhandler = (SAXHandlerBase)m_saxhandlerClass.newInstance();
            saxhandler.setOutputStream(m_outputstream);
            // parse input
            saxParser.parse(m_inputstream, saxhandler);
            //input.close();
            //input=null;
        }catch(ParseEndException exc)
        {
            //the exception used to exit the parsing, ignore
        }
        catch(Exception exc){
            Log.log(m_serverName, Log.Type.NOTICE, exc);
            return;
        }
    }
    
    /*protected InputStream bufferInputstream(InputStream inputstream) throws Exception{
        int count = 0;
        while(true){
            int numread = inputstream.read(m_buffer,count,m_buffer.length-count);
            if( numread==-1 )
                break;
            count += numread;
            if( m_buffer[count-1] == '>' )
                break;
        }
        //System.out.println( new String(m_buffer, 0, count, Config.getCharset()) );
        
        ByteArrayInputStream stream = new ByteArrayInputStream(m_buffer, 0, count);
        return stream;
    }*/
    
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
    /**
     * get server name
     * @return 
     */
    public String getServerName() {
        return m_serverName;
    }
}

package com.bizdata.campux.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * HandlerBase implements common functions of a server handler
 * @author yuy
 */
public abstract class ServerBase extends Thread{
    // the input stream from network client
    protected InputStream m_inputstream = null;
    // the output stream to the network client
    protected OutputStream m_outputstream = null;
    
    protected CommonServer m_commonserver=null;
    
    /**
     * The CommonServer will call this interface when it gets requests
     * @param input input stream
     * @throws Exception 
     */
    public void handle(InputStream input, OutputStream output) throws Exception{
        ServerBase h = this.getClass().newInstance();
        h.m_inputstream = input;
        h.m_outputstream = output;
        h.start();        
    }
    
    /**
     * subclass should implement it functionality in the run() function
     */
    @Override
    abstract public void run();
    
    /**
     * subclass sould implement how to start the server, the common code is
     * public void startServer(){
     *   m_commonserver = new CommonServer();
     *   m_commonserver.startServer(s_port, this);
     * }    
     */
    abstract public void startServer();
    
    /**
     * common stop process
     */
    public void stopServer(){
        m_commonserver.stopServer();
    }
    
    /**
     * read a network input stream
     */
    protected String readInput() throws Exception{
        BufferedInputStream reader = new BufferedInputStream(m_inputstream);
        boolean end = false;
        byte[] buff = new byte[10240];
        byte[] tag = new byte[4]; //test for </x>
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(10240);
        try{
            while(!end){
                int size = reader.read(buff, 0, buff.length);
                if( size > 0 ){
                    buffer.write(buff, 0, size);
                    // store the last 4 bytes for test of the end
                    if( size>=4){
                        System.arraycopy(buff, size-4, tag, 0, 4);
                    }else{
                        System.arraycopy(tag, size, tag, 0, 4-size);
                        System.arraycopy(buff, 0, tag, 4-size, size);
                    }
                    if( tag[0]=='<' && tag[1]=='/' && tag[2]=='x' && tag[3]=='>'){
                        end = true;
                    }
                }else{
                    this.sleep(100);
                }                
            }
        }catch(Exception exc){
            Log.log("ServerUserprofile", Log.Type.NOTICE, exc);
        }
        new String("UTF-8");
        return buffer.toString("UTF-8");
    }
}

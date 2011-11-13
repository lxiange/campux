/**
 * CommonServer implements the overhead of the communication. Any server only 
 * needs to implement the handler by inheriting the HandlerBase class, and then 
 * call the 
 * \code[startServer(int port, Handler handler)]
 * method to pass in itself. 
 */
package com.bizdata.campux.server;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
/**
 *
 * @author yuy
 */
public class CommonServer extends Thread{
    protected SSLServerSocket m_sslserversocket = null;
    protected int m_port = -1;
    protected ServerBase m_handler = null;
    
    @Override
    public void run() {
        try{
            SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            m_sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(m_port);
        }catch(Exception exc){
            Log.log("server", Log.Type.FATAL, exc);
            return;
        }
        
        while(m_sslserversocket!=null){
            try{
                SSLSocket sslsocket = (SSLSocket) m_sslserversocket.accept();

                InputStream inputstream = sslsocket.getInputStream();
                OutputStream outputstream = sslsocket.getOutputStream();
                
                m_handler.handle(inputstream, outputstream);
                
            }catch(Exception exc){
                Log.log("server", Log.Type.NOTICE, exc);
            }
        }
    }
    /**
     * instance of the server
     */
    private CommonServer m_instance = null;
    /**
     * Start the server that listens the port
     * @param port the tcp port to be listened on
     */
    public void startServer(int port, ServerBase handler){
        stopServer();
        
        try{
            m_instance = this.getClass().newInstance();
        }catch(Exception exc){
            Log.log("server", Log.Type.FATAL, exc);
        }
        
        m_instance.m_port = port;
        m_instance.m_handler = handler;
        m_instance.start();
    }
    /**
     * shutdown the server
     */
    public void stopServer(){
        if( m_instance!=null ){
            try{
                m_instance.m_sslserversocket.close();
            }catch(Exception exc){
                Log.log("server", Log.Type.NOTICE, exc);
            }
            m_instance.m_sslserversocket = null;
        }
    }
}

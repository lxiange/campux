/*
 * Copyright (C) 2011 Nanjing Bizdata-infotech co., ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bizdata.campux.server;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
/**
 * CommonServer implements the overhead of the communication. Any server only 
 * needs to implement the handler by inheriting the HandlerBase class, and then 
 * call the 
 * \code[startServer(int port, Handler handler)]
 * method to pass in itself. 
 */
public class CommonServer extends Thread{
    protected SSLServerSocket m_sslserversocket = null;
    protected int m_port = -1;
    protected ServerBase m_server = null;
    
    @Override
    public void run() {
        try{
            //setup ssl server socket
            String keyFile = Config.getValue("SSLServerCertificate");
            String keyFilePass = Config.getValue("SSLServerCertificateKey");
            String keyPass = Config.getValue("SSLServerAliasKey");
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(keyFile), keyFilePass.toCharArray()); 
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); 
            kmf.init(ks,keyPass.toCharArray());
            SSLContext sslc = SSLContext.getInstance("SSLv3"); 
            sslc.init(kmf.getKeyManagers(), null, null); 
            SSLServerSocketFactory sslserversocketfactory = sslc.getServerSocketFactory(); 

            //Listen
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
                
                m_server.handle(inputstream, outputstream);
                
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
    public void startServer(int port, ServerBase server){
        stopServer();
        
        try{
            m_instance = this.getClass().newInstance();
        }catch(Exception exc){
            Log.log("server", Log.Type.FATAL, exc);
        }
        
        m_instance.m_port = port;
        m_instance.m_server = server;
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

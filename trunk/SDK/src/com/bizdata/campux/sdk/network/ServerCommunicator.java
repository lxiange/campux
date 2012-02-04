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
package com.bizdata.campux.sdk.network;

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.sdk.Log;
import com.bizdata.campux.sdk.Log.Type;
import com.bizdata.campux.sdk.exception.NetworkErrorException;
import com.bizdata.campux.sdk.exception.ServerOutofreachException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyStore;
//import javax.net.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.BasicHttpParams;


/**
 * 
 * @author yuy
 */
public class ServerCommunicator {
    protected Socket m_socket;
    protected InputStream m_inputstream;
    protected OutputStream m_outputstream;
    static public enum StoreType{
        JKS, BKS, PKCS, JCEKS
    }
    /**
     * In the instantiation, the communicator create a TCP connection to the server.
     * @param port
     * @throws Exception 
     */
    public ServerCommunicator(int port) throws Exception{
        try{
            String storetype = Config.getValue("KeyStoreType");
            String host = Config.getValue("ServerAddress");
            String password = Config.getValue("SSLClientCertificateKey");
            
            // when the KeyStoreType is not specified or is "jsk", use Oracle default setting, otherwise, use android compatible setting
            if( storetype==null || "jsk".equalsIgnoreCase(storetype)){
                InetSocketAddress serveraddress = new InetSocketAddress(host, port);
                System.setProperty("javax.net.ssl.trustStore",Config.getValue("SSLClientCertificate"));
                System.setProperty("javax.net.ssl.trustStorePassword",Config.getValue("SSLClientCertificateKey"));
            
                javax.net.ssl.SSLSocketFactory sslsf = (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault();
                m_socket = sslsf.createSocket(host, port);
            }else{
                KeyStore trusted = KeyStore.getInstance(storetype);
                trusted.load(new FileInputStream(Config.getValue("SSLClientCertificate")), password.toCharArray());
                
                SSLSocketFactory sslsf = new SSLSocketFactory(trusted);
                // the deprecated methods are used to be compatible with android 2.2
                sslsf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                m_socket = sslsf.createSocket();
                m_socket = sslsf.connectSocket(m_socket, host, port, null, -1, new BasicHttpParams());
            }
            
            m_inputstream = new BufferedInputStream(m_socket.getInputStream());
            m_outputstream = new BufferedOutputStream(m_socket.getOutputStream());
        }catch(Exception exc){
            Log.log("ServerCommunication", Type.ERROR, exc);
            throw(new ServerOutofreachException(exc));
        }
    }
    /**
     * get the input stream of the socket
     * @return 
     */
    public InputStream getInputStream(){
        return m_inputstream;
    }
    /**
     * get the output stream of the socket
     * @return 
     */
    public OutputStream getOutputStream(){
        return m_outputstream;
    }
    /**
     * send a String into the output stream
     * @param str
     * @throws Exception 
     */
    public void sentString(String str) throws Exception{
        OutputStream os = getOutputStream();
        if( os==null )
            return;
        OutputStreamWriter output = new OutputStreamWriter(getOutputStream());
        output.write(str);
        output.flush();
    }
    /**
     * close the socket together with the input and output stream
     */
    public void close(){
        try{
            m_inputstream.close();
        }catch(Exception exc){
            Log.log("ServerCommunication", Type.NOTICE, exc);
        }
        try{
            m_outputstream.close();
        } catch (Exception exc) {
            Log.log("ServerCommunication", Type.NOTICE, exc);
        }
        try{
            m_socket.close();
        } catch (Exception exc) {
            Log.log("ServerCommunication", Type.NOTICE, exc);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.sdk.network;

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.sdk.Log;
import com.bizdata.campux.sdk.Log.Type;
import com.bizdata.campux.sdk.exception.ServerOutofreachException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author yuy
 */
public class ServerCommunicator {
    protected Socket m_socket;
    protected InputStream m_inputstream;
    protected OutputStream m_outputstream;
    public ServerCommunicator(int port) throws Exception{
        try{
            System.setProperty("javax.net.ssl.trustStore",Config.getValue("SSLClientCertificate"));
            System.setProperty("javax.net.ssl.trustStorePassword",Config.getValue("SSLClientCertificateKey"));
            SSLSocketFactory sslsf = (SSLSocketFactory)SSLSocketFactory.getDefault();
            m_socket = sslsf.createSocket(Config.getValue("ServerAddress"),port);
            
            m_inputstream = new BufferedInputStream(m_socket.getInputStream());
            m_outputstream = new BufferedOutputStream(m_socket.getOutputStream());
        }catch(Exception exc){
            Log.log("ServerCommunication", Type.ERROR, exc);
            throw(new ServerOutofreachException(exc));
        }
    }
    public InputStream getInputStream(){
        return m_inputstream;
    }
    public OutputStream getOutputStream(){
        return m_outputstream;
    }
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

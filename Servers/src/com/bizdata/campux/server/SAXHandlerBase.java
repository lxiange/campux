package com.bizdata.campux.server;

import com.bizdata.campux.server.Log.Type;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handle the received commands in xml format through SAX
 * @author yuy
 */
public class SAXHandlerBase extends DefaultHandler{
    
    // output to the network client
    BufferedWriter m_output = null;
    
    public void setOutputStream(OutputStream outputstream){
         m_output = new BufferedWriter(new OutputStreamWriter(outputstream));
    }
    /**
     * send response back to the client
     * @param str 
     */
    protected void response(String str){
        if( Config.isUnitTest())
            System.out.println(str);
        else{
        try{
            m_output.write(Config.getXMLfirstline());
            m_output.write(str);
            m_output.flush();
            m_output.close();
        }catch(Exception exc){
            Log.log("UserProfile", Type.ERROR, exc);
            try{m_output.close();}catch(Exception e){}
        }}
    }
    /**
     * send error back to the client
     * @param code
     * @param str 
     */
    protected void responseError(int code, String str){
        if( Config.isUnitTest())
            System.out.println(str);
        else{
        try{
            m_output.write(Config.getXMLfirstline());
            m_output.write("<err c=\""+code+"\">"+str+"</err>");
            m_output.close();
        }catch(Exception exc){
            Log.log("UserProfile", Type.ERROR, exc);
            try{m_output.close();}catch(Exception e){}
        }}
    }
}

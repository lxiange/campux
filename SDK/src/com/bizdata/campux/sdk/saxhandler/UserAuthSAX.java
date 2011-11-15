package com.bizdata.campux.sdk.saxhandler;

import java.io.InputStream;
import java.util.LinkedList;
import org.xml.sax.Attributes;

/**
 *
 * @author yuy
 */
public class UserAuthSAX extends SAXHandlerBase {    
    protected LinkedList<String> m_vars = new LinkedList();
    protected String m_responseStr = null;
    
    public String getResponseString(){
        return m_responseStr;
    }
    
    public String[] getList(){
        String[] vs = new String[m_vars.size()];
        for(int i=0; i<vs.length; i++){
            vs[i] = m_vars.get(i);
        }
        return vs;
    }
    
    @Override
    protected void contentReceived(String content, String tagname, Attributes m_tagattr){
        System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }else if( "g".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }else if( "u".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }
    }
}

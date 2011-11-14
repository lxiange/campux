/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.sdk.saxhandler;

import java.io.InputStream;
import java.util.LinkedList;
import org.xml.sax.Attributes;

/**
 *
 * @author yuy
 */
public class UserStatusSAX extends SAXHandlerBase{    
    protected LinkedList<String> m_vars = new LinkedList();
    
    public String[] getVariables(){
        String[] vs = new String[m_vars.size()];
        for(int i=0; i<vs.length; i++){
            vs[i] = m_vars.get(i);
        }
        return vs;
    }
    
    @Override
    protected void contentReceived(String content, String tagname, Attributes m_tagattr){
        if( "v".equalsIgnoreCase(tagname) ){
            m_vars.add(content.trim());
        }
    }
}

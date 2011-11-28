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
package com.bizdata.campux.sdk.saxhandler;

import com.bizdata.campux.sdk.Config;
import java.io.InputStream;
import java.util.LinkedList;
import org.xml.sax.Attributes;

/**
 * handles the 
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
    protected void contentReceived(String content, String tagname, Attributes tagattr){
        System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }else if( "g".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }else if( "u".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }
    }
    
    public String prepareLogin(String user, String psw){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<v><u>");
        str.append(user);
        str.append("</u><p>");
        str.append( psw );
        str.append( "</p></v>\r\n" );
        return str.toString();
    }
    
    public String prepareLogout(String sessionID){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<o>");
        str.append(sessionID);
        str.append("</o>\r\n" );
        return str.toString();
    }
    
    public String prepareRegistration(String user, String psw){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<a><u>");
        str.append(user);
        str.append("</u><p>");
        str.append(psw);
        str.append("</p></a>\r\n");
        return str.toString();
    }
    
    public String prepareLookup(String sessionID){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<c><s>");
        str.append(sessionID);
        str.append("</s></c>\r\n");
        return str.toString();
    }
}

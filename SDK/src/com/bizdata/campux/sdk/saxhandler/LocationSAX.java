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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.xml.sax.Attributes;



/**
 *
 * @author yuy
 * @date 2011-12-21 08:51:47
 */
public class LocationSAX extends SAXHandlerBase {
    protected String m_responseStr = null;
    
    public String getResponseString(){
        return m_responseStr;
    }
    
    @Override
    protected void contentReceived(String content, String tagname){
        System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }
    }

    public String prepareGetLocation(String sessionID, String ip, HashMap<String, Integer> wifis, String connectedWifi){
        StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        str.append("<g s=\"");
        str.append(sessionID);
        str.append("\">");
        if( ip!=null){
            str.append("<i>");
            str.append(ip);
            str.append("</i>");
        }
        for(Entry<String, Integer> wifi : wifis.entrySet()){
            str.append("<w s=\"");
            str.append(wifi.getValue());
            str.append("\"");
            if( wifi.getKey().equals(connectedWifi)){
                str.append(" c=\"true\"");
            }
            str.append(">");
            str.append(wifi.getKey());
            str.append("</w>");
        }
        str.append("</g>");
        return str.toString();
    }
    
    public String prepareAddLocation(String sessionID, String lname, String ip, HashMap<String, Integer> wifis, String connectedWifi)
    {
    	StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        str.append("<a s=\"");
        str.append(sessionID);
        str.append("\" l=\"");
        str.append(lname);
        str.append("\">");
        if( ip!=null){
            str.append("<i>");
            str.append(ip);
            str.append("</i>");
        }
        for(Entry<String, Integer> wifi : wifis.entrySet()){
            str.append("<w s=\"");
            str.append(wifi.getValue());
            str.append("\"");
            if( wifi.getKey().equals(connectedWifi)){
                str.append(" c=\"true\"");
            }
            str.append(">");
            str.append(wifi.getKey());
            str.append("</w>");
        }
        str.append("</a>");
        return str.toString();
    }
    
}

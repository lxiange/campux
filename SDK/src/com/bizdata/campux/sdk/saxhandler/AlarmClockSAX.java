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

import java.io.InputStream;
import java.util.LinkedList;

import org.xml.sax.Attributes;

/**
 *
 * @author yuy
 */
public class AlarmClockSAX extends SAXHandlerBase{ 
    protected LinkedList<String> m_vars = new LinkedList<String>();
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
    protected void contentReceived(String content, String tagname) {
		System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }else if( "s".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }else if( "p".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }else if( "d".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }
    }    
	
	public String prepareSingleACSet(String sessionID,String c_user,String c_time,String c_content)
	{
		StringBuilder str = new StringBuilder();
    	str.append("<sas ");
        str.append("s=\""+sessionID+"\">");
        str.append("<s u=\""+c_user+"\" t=\""+c_time+"\">");
        str.append("c_content");
        str.append("</s>");
        str.append("</sas>\r\n");
        return str.toString();
	}

	public String prepareMultipleACSet(String sessionID,String c_user,String c_time,
			String c_initialDate,String c_interval,String c_content)
	{
		StringBuilder str = new StringBuilder();
    	str.append("<sap ");
        str.append("s=\""+sessionID+"\">");
        str.append("<p u=\""+c_user+"\" t=\""+c_time+"\" s=\""+c_initialDate+"\" i=\""+c_interval+"\">");
        str.append("c_content");
        str.append("</p>");
        str.append("</sap>\r\n");
        return str.toString();
	}

	public String prepareACEnum(String sessionID)
	{
		StringBuilder str = new StringBuilder();
    	str.append("<al ");
        str.append("s=\""+sessionID+"\">");
        str.append("</al>\r\n");
        return str.toString();
	}
	
	public String prepareACDelete(String sessionID,String c_alarmID)
	{
		StringBuilder str = new StringBuilder();
    	str.append("<sa ");
        str.append("s=\""+sessionID+"\">");
        str.append(c_alarmID);
        str.append("</sa>\r\n");
        return str.toString();
	}
	
/*	public static void main(String[] args){
		AlarmClockSAX ac=new AlarmClockSAX();
		String sessionID="1234556677";
		System.out.println(ac.prepareSingleACSet(sessionID, "a", "b", "c"));
		System.out.println(ac.prepareMultipleACSet(sessionID, "a", "b", "c", "d", "e"));
		System.out.println(ac.prepareACEnum(sessionID));
		System.out.println(ac.prepareACDelete(sessionID, "g"));
	}
*/	
}

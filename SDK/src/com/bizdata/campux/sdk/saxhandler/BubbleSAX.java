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
public class BubbleSAX extends SAXHandlerBase{
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
    protected void contentReceived(String content, String tagname, Attributes m_tagattr) {
		System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }else if( "m".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }
    }    
	
	public String prepareBubbleRelease(String sessionID,String b_location,String b_content)
	{
		StringBuilder str = new StringBuilder();
    	str.append("<p ");
        str.append("s=\""+sessionID+"\" ");
        str.append("b64=\"true\" l=\""+b_location+"\">");
        str.append(b_content);
        str.append("</p>\r\n");
        return str.toString();
	}
	
	public String prepareBubbleHistoryRead(String sessionID,String b_initialTime,String b_location)
	{
		StringBuilder str = new StringBuilder();
    	str.append("<r ");
        str.append("s=\""+sessionID+"\" ");
        str.append("d=\""+b_initialTime+"\">");
        str.append(b_location);
        str.append("</r>\r\n");
        return str.toString();
	}
	
	
/*	public static void main(String[] args){
		String sessionID="1234556677";
		BubbleSAX bb=new BubbleSAX();
		System.out.println(bb.prepareBubbleRelease(sessionID, "a", "b"));
		System.out.println(bb.prepareBubbleReceive(sessionID, "c", "d"));
	}
*/
}

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

import com.bizdata.campux.sdk.BubbleMessage;
import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.io.InputStream;
import java.util.LinkedList;

import org.xml.sax.Attributes;

/**
 *
 * @author yuy
 */
public class BubbleSAX extends SAXHandlerBase{
    protected LinkedList<BubbleMessage> m_vars = new LinkedList<BubbleMessage>();
    protected String m_responseStr = null;
    
    public String getResponseString(){
        return m_responseStr;
    }
    
    public BubbleMessage[] getList(){
        BubbleMessage[] vs = new BubbleMessage[m_vars.size()];
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
        }else if( "m".equalsIgnoreCase(tagname) ){
            BubbleMessage msg = new BubbleMessage();
            msg.message = content;
            msg.publisher = m_moreatt.get("u");
            msg.time = Long.parseLong(m_moreatt.get("d"));
            m_vars.add(msg);
        }
    }    
	
	public String prepareBubblePublish(String sessionID,String content)
	{
            String val = DatatypeConverter.printBase64Binary(content.getBytes(Config.getCharset()));
            
            StringBuilder str = new StringBuilder();
            str.append("<p ");
            str.append("s=\""+sessionID+"\" ");
            str.append("b64=\"true\">");
            str.append(val);
            str.append("</p>");
            return str.toString();
	}
	
	public String prepareBubbleRead(String sessionID, long time)
	{
            StringBuilder str = new StringBuilder();
            str.append("<r ");
            str.append("s=\""+sessionID+"\" ");
            str.append("d=\""+time+"\">");
            str.append("</r>");
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

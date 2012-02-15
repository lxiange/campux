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
public class MediaSAX extends SAXHandlerBase{
	protected LinkedList<String> m_vars = new LinkedList<String>();
    protected String m_responseStr = null;
    /**
     * get names of the variables in the system
     * @return 
     */
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
    /**
     * Deals with the XML content via SAX
     * @param content
     * @param tagname
     * @param m_tagattr 
     */
    @Override
    protected void contentReceived(String content, String tagname){
    	System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }else if( "a".equalsIgnoreCase(tagname) ){
            m_vars.add(content.trim());
        }else if( "f".equalsIgnoreCase(tagname) ){
            m_vars.add(content.trim());
        }else if( "i".equalsIgnoreCase(tagname) ){
            m_vars.add(content.trim());
        }else if( "s".equalsIgnoreCase(tagname) ){
            m_vars.add(content.trim());
        }
    }

    public String prepareMediaListGet(String sessionID)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<la ");
        str.append("s=\""+sessionID+"\">");
        str.append("</la>\r\n");
        return str.toString();
    }
    
    public String prepareMediaContentRead(String sessionID,String m_content)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<ra ");
        str.append("s=\""+sessionID+"\">");
        str.append(m_content);
        str.append("</ra>\r\n");
        return str.toString();
    }
    
    public String prepareMusicRead(String sessionID,String m_ID)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<rs ");
        str.append("s=\""+sessionID+"\">");
        str.append(m_ID);
        str.append("</rs>\r\n");
        return str.toString();
    }

    
 /*   public static void main(String[] args){
    	String sessionID="1234556677";
    	MediaSAX mm=new MediaSAX();
    	System.out.println(mm.prepareMediaListGet(sessionID));
    	System.out.println(mm.prepareMediaContentRead(sessionID, "aa"));
    	System.out.println(mm.prepareMusicRead(sessionID, "bb"));
    }
*/
}

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
public class ClassScheduleSAX extends SAXHandlerBase{
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
    protected void contentReceived(String content, String tagname, Attributes m_tagattr){
    	System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }else if( "c".equalsIgnoreCase(tagname) ){
            m_vars.add(content.trim());
        }
    }
    
    public String prepareClassListGet()
    {
    	StringBuilder str = new StringBuilder();
        str.append("<lc>");
        str.append("</lc>\r\n");
        return str.toString();
    }
    
    public String prepareClassPublish(String sessionID,String address,String available)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<pr ");
        str.append("s=\""+sessionID+"\" ");
        str.append("l=\""+address+"\">");
        str.append(available);
        str.append("</pr>\r\n");
        return str.toString();
    }
    
    public String prepareClassHistoryRead(String sessionID,String address)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<rr ");
        str.append("s=\""+sessionID+"\" ");
        str.append("l=\""+address+"\">");
        str.append("</rr>\r\n");
        return str.toString();
    }
  
    public String prepareClassScheduleRead(String sessionID)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<rs ");
        str.append("s=\""+sessionID+"\">");
        str.append("</rs>\r\n");
        return str.toString();
    }
    
    public String prepareClassScheduleSet(String sessionID,String c_day,String c_class,String c_room,String c_content)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<ws ");
        str.append("s=\""+sessionID+"\" ");
        str.append("d=\""+c_day+"\" ");
        str.append("t=\""+c_class+"\" ");
        str.append("r=\""+c_room+"\">");
        str.append(c_content);
        str.append("</ws>\r\n");
        return str.toString();
    }
    
    
 /*   public static void main(String[] args){
    	String sessionID="1234556677";
    	ClassScheduleSAX cc=new ClassScheduleSAX();
  	    System.out.println(cc.prepareClassListGet());
    	System.out.println(cc.prepareClassPublish(sessionID, "bb", "cc"));
    	System.out.println(cc.prepareClassHistoryRead(sessionID, "dd"));
    	System.out.println(cc.prepareClassScheduleRead(sessionID));
    	System.out.println(cc.prepareClassScheduleSet(sessionID, "ee", "ff", "gg", "hh"));
 
    }
 */   
}

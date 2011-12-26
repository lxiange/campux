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
 * SAX handler
 * @author yuy
 */
public class UserInfoSAX extends SAXHandlerBase{
	
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
        }else if( "a".equalsIgnoreCase(tagname) ){
            m_vars.add(content.trim());
        }else if( "i".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }else if( "t".equalsIgnoreCase(tagname) ){
            m_vars.add(content);            
        }else if( "d".equalsIgnoreCase(tagname) ){
            m_vars.add(content);            
        }else if( "u".equalsIgnoreCase(tagname) ){
            m_vars.add(content);            
        }else if( "co".equalsIgnoreCase(tagname) ){
            m_vars.add(content);            
        }else if( "e".equalsIgnoreCase(tagname) ){
            m_vars.add(content);            
        }else if( "c".equalsIgnoreCase(tagname) ){
            m_vars.add(content);            
        }
    }
    
    public String preparePublisherList()
    {
    	StringBuilder str = new StringBuilder();
        str.append("<lp>");
        str.append("</lp>\r\n");
        return str.toString();
    }
   
    public String preparePublisherRegistration(String sessionID,String p_name,
    		String icon, String p_displayName, String[] p_infoKind)
    {
    	StringBuilder str = new StringBuilder();
        str.append("<rp s=\"");
        str.append("sessionID\" n=\"");
        str.append(p_name+"\">");
        str.append("<i b64=\"true\">"+icon+"</i>");
        str.append("<d>"+p_displayName+"</d>");
        for(String kind : p_infoKind){
        	str.append("<c>");
            str.append(kind);
            str.append("</c>");        
        }
        str.append("<rp>");
        return str.toString();
    }

    public String prepareAccountInitialization(String sessionID)
    {
    	StringBuilder str = new StringBuilder();
        str.append("<ui ");
        str.append("s=\""+sessionID+"\">");
        str.append("</ui>\r\n");
        return str.toString();
    }
    
    public String prepareUpdateCheck(String sessionID)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<ci ");
        str.append("s=\""+sessionID+"\">");
        str.append("</ci>\r\n");
        return str.toString();
    }

    public String prepareInfoAchieve(String sessionID,String initialInfoID)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<gs ");
        str.append("s=\""+sessionID+"\">");
        str.append(initialInfoID);
        str.append("</gs>\r\n");
        return str.toString();
    }
    
    public String prepareDetailedInfoAchieve(String sessionID,String infoID)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<gi ");
        str.append("s=\""+sessionID+"\">");
        str.append(infoID);
        str.append("</gi>\r\n");
        return str.toString();
    }

    public String prepareInfoPublish(String sessionID,String i_title,String i_date,
    		String i_link,String i_content,String i_kind,String a_name,String a_date,String a_address)
    {
    	StringBuilder str = new StringBuilder();
        str.append("<si s=\""+sessionID+"\">");
        str.append("<t b64=\"true\">"+i_title+"</t>");
        str.append("<d>"+i_date+"</d>");
        str.append("<u>"+i_link+"</u>");
        str.append("<co b64=\"true\">"+i_content+"</co>");
        str.append("<c>"+i_kind+"</c>");
        str.append("<e>");
        str.append("<t b64=\"true\">"+a_name+"</t>");
        str.append("<d>"+a_date+"</d>");
        str.append("<t b64=\"true\">"+a_address+"</t>");
        str.append("</e>");
        str.append("</si>");
        return str.toString();
    }

    public String prepareInfoDelete(String sessionID,String infoID)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("<di ");
        str.append("s=\""+sessionID+"\">");
        str.append(infoID);
        str.append("</di>\r\n");
        return str.toString();
    }

    
 /*   public static void main(String[] args){
    	UserInfoSAX ui=new UserInfoSAX();
    	String sessionID="1234556677";
    	String infoID="INFOA";
    	String[] pt={"xx","yy","zz"};
    	System.out.println(ui.preparePublisherList());
    	System.out.println(ui.prepareAccountInitialization(sessionID));
    	System.out.println(ui.prepareUpdateCheck(sessionID));
    	System.out.println(ui.prepareInfoAchieve(sessionID,infoID));
    	System.out.println(ui.prepareInfoDelete(sessionID,infoID));
    	System.out.println(ui.preparePublisherRegistration(sessionID,"a","b","c",pt));
    	System.out.println(ui.prepareInfoPublish(sessionID,"a","b","c","d","e","f","g","h"));
    }
*/
}



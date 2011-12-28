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
import com.bizdata.campux.sdk.*;
/**
 * SAX handler
 * @author yuy
 */
public class UserInfoSAX extends SAXHandlerBase{
	
    protected String m_responseStr = null;
    protected LinkedList<Publisher> m_pubs=new LinkedList<Publisher>();
    protected LinkedList<InfoMessage> m_msgs=new LinkedList<InfoMessage>();
    protected LinkedList<InfoMsgIndex> m_indices=new LinkedList<InfoMsgIndex>();
    protected Publisher pub=null;
    protected InfoMessage msg=null;
    protected InfoMsgIndex index=null;
    protected InfoEvent event=null;
    /**
     * get infomessages
     * @return 
     */
    public InfoMessage[] getInfoMessages(){
    	InfoMessage[] msgs = new InfoMessage[m_msgs.size()];
        for(int i=0; i<msgs.length; i++){
            msgs[i] = m_msgs.get(i);
        }
        return msgs;
    }
    /**
    * get publishers
    * @return 
    */
    public Publisher[] getPublishers(){
	   Publisher[] pubs = new Publisher[m_pubs.size()];
       for(int i=0; i<pubs.length; i++){
    	   pubs[i] = m_pubs.get(i);
       }
       return pubs;
    }
    /**
     * get message indices
     * @return 
     */
     public InfoMsgIndex[] getMessageIndices(){
    	InfoMsgIndex[] indices = new InfoMsgIndex[m_indices.size()];
        for(int i=0; i<indices.length; i++){
        	indices[i] = m_indices.get(i);
        }
        return indices;
     }
    
   /**
     * get value of response with singleton string
     * @return 
     */
    public String getResponseString(){
        return m_responseStr;
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
            m_responseStr = content.trim();
        }else if( "a".equalsIgnoreCase(tagname) ){
            pub=new Publisher();
            pub.m_user=m_tagattr.getValue("n");
        }else if( "ip".equalsIgnoreCase(tagname) ){
        	pub.p_iconname=content.trim();
        }else if( "dp".equalsIgnoreCase(tagname) ){
        	pub.p_displayname=content.trim();
        }else if( "cp".equalsIgnoreCase(tagname) ){
        	pub.p_infotype.add(content.trim());
        	while("cp".equalsIgnoreCase(tagname) )
        		pub.p_infotype.add(content.trim());
        	m_pubs.add(pub);
        }else if( "i".equalsIgnoreCase(tagname) ){
            index=new InfoMsgIndex();
            index.i_preview=content.trim();
            index.i_publisher=m_tagattr.getValue("xp");
            index.i_pubcategory=m_tagattr.getValue("xc");
            try{
                index.i_id = Integer.parseInt(m_tagattr.getValue("xd"));
            }catch(Exception exc){
            	index.i_id = -1;
            }
            m_indices.add(index);
        }else if( "t".equalsIgnoreCase(tagname) ){
        	msg=new InfoMessage();
        	msg.m_title=content.trim();
        }else if( "d".equalsIgnoreCase(tagname) ){
        	msg.m_date=content.trim();
        }else if( "l".equalsIgnoreCase(tagname) ){
        	msg.m_link=content.trim();
        }else if( "co".equalsIgnoreCase(tagname) ){
        	msg.m_content=content.trim();
        }else if( "e".equalsIgnoreCase(tagname) ){
        	event= new InfoEvent();
        }else if( "et".equalsIgnoreCase(tagname) ){
        	event.event_title=content.trim();
        }else if( "ed".equalsIgnoreCase(tagname) ){
        	event.event_date=content.trim();
        }else if( "ea".equalsIgnoreCase(tagname) ){
        	event.event_place=content.trim();
        	msg.events.add(event);
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
        str.append("<ip b64=\"true\">"+icon+"</ip>");
        str.append("<dp>"+p_displayName+"</dp>");
        for(String kind : p_infoKind){
        	str.append("<cp>");
            str.append(kind);
            str.append("</cp>");        
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
    		String i_link,String i_content,String i_publisher,String i_kind,InfoEvent[] ee,String[] uu,String[] gg)
    {
    	StringBuilder str = new StringBuilder();
        str.append("<si s=\""+sessionID+"\">");
        str.append("<t b64=\"true\">"+i_title+"</t>");
        str.append("<d>"+i_date+"</d>");
        str.append("<l>"+i_link+"</l>");
        str.append("<co b64=\"true\">"+i_content+"</co>");
        str.append("<p>"+i_publisher+"</p>");
        str.append("<c>"+i_kind+"</c>");
        for(int i=0;i<ee.length;i++){        	
        	str.append("<e>");
            str.append("<et b64=\"true\">"+ee[i].event_title+"</et>");
            str.append("<ed>"+ee[i].event_date+"</ed>");
            str.append("<ea b64=\"true\">"+ee[i].event_place+"</ea>");
            str.append("</e>");
        }
        for(String user : uu){
            str.append("<u>");
            str.append(user);
            str.append("</u>");
        }
        for(String group : gg){
            str.append("<g>");
            str.append(group);
            str.append("</g>");
        }        
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

}



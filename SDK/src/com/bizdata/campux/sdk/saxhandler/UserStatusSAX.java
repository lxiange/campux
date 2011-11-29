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
import com.bizdata.campux.sdk.Message;
import java.util.LinkedList;
import javax.xml.bind.DatatypeConverter;
import org.xml.sax.Attributes;

/**
 *
 * @author yuy
 */
public class UserStatusSAX extends SAXHandlerBase{    
    protected LinkedList<String> m_vars = new LinkedList<String>();
    protected LinkedList<Message> m_msgs = new LinkedList<Message>();
    protected String m_val = null;
    /**
     * get names of the variables in the system
     * @return 
     */
    public String[] getVariables(){
        String[] vs = new String[m_vars.size()];
        for(int i=0; i<vs.length; i++){
            vs[i] = m_vars.get(i);
        }
        return vs;
    }
    /**
     * get messages
     * @return 
     */
    public Message[] getMessages(){
        Message[] msgs = new Message[m_msgs.size()];
        for(int i=0; i<msgs.length; i++){
            msgs[i] = m_msgs.get(i);
        }
        return msgs;
    }
    /**
     * get a String value
     * @return 
     */
    public String getValue()
    {
        return m_val;
    }
    /**
     * Deals with the XML content via SAX
     * @param content
     * @param tagname
     * @param m_tagattr 
     */
    @Override
    protected void contentReceived(String content, String tagname, Attributes tagattr){
        if("ok".equalsIgnoreCase(tagname)){
            m_val = content;
        }else if( "v".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }else if( "b".equalsIgnoreCase(tagname) ){
            Message msg = new Message();
            msg.message = content;
            msg.date = tagattr.getValue("t");
            msg.publisher = tagattr.getValue("u");
            try{
                msg.id = Integer.parseInt(tagattr.getValue("d"));
            }catch(Exception exc){
                msg.id = -1;
            }
            m_msgs.add(msg);
        }
    }
    /**
     * construct the XML for the command getUserVariables
     * @return 
     */
    public String prepareUserVariables(){
        String str = Config.getXMLfirstline();
        str += "<usl></usl>";
        return str;
    }
    /**
     * construct the XML for the command setUserVariable
     * @param sessionID
     * @param var
     * @param val
     * @return 
     */
    public String prepareSetUserVariable(String sessionID, String var, String val){
        StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        
        val = DatatypeConverter.printBase64Binary(val.getBytes(Config.getCharset()));
        
        str.append( "<usw s=\"" );
        str.append( sessionID );
        str.append( "\" n=\"");
        str.append( var );
        str.append( "\" b64=\"true\">");
        str.append( val );
        str.append( "</usw>" );
        return str.toString();
    }
    /**
     * construct the XML for the command getUserVariable
     * @param sessionID
     * @param var
     * @param val
     * @return 
     */
    public String prepareGetUserVariable(String sessionID, String var){
        StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        str.append("<usr s=\"");
        str.append(sessionID);
        str.append( "\">" );
        str.append( var );
        str.append( "</usr>" );
        return str.toString();
    }
    /**
     * construct the XML for the command getMessage
     * @param sessionID
     * @param lastMsgID
     * @return 
     */
    public String prepareGetMessage(String sessionID, int lastMsgID){
        StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        str.append("<mg s=\"");
        str.append(sessionID);
        str.append("\">");
        str.append(lastMsgID);
        str.append("<mg>");
        return str.toString();
    }
    /**
     * construct the XML for the command delete message
     * @param sessionID
     * @param msgID
     * @return 
     */
    public String prepareDeleteMessage(String sessionID, int msgID){
        StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        str.append("<md s=\"");
        str.append(sessionID);
        str.append("\">");
        str.append(msgID);
        str.append("<md>");
        return str.toString();
    }
    /**
     * construct the XML for the command putMessage
     * @param sessionID
     * @param users
     * @param groups
     * @param message
     * @return 
     */
    public String preparePutMessage(String sessionID, String[] users, String[] groups, String message){
        StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        str.append("<ms s=\"");
        str.append(sessionID);
        str.append("\">");
        for(String user : users){
            str.append("<u>");
            str.append(user);
            str.append("</u>");
        }
        for(String group : groups){
            str.append("<g>");
            str.append(group);
            str.append("</g>");
        }        
        str.append("<m b64=\"true\">");
        message = DatatypeConverter.printBase64Binary(message.getBytes(Config.getCharset()));
        str.append(message);
        str.append("</m><ms>");
        return str.toString();
    }
}

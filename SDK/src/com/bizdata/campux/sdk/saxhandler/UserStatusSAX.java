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
import javax.xml.bind.DatatypeConverter;
import org.xml.sax.Attributes;

/**
 *
 * @author yuy
 */
public class UserStatusSAX extends SAXHandlerBase{    
    protected LinkedList<String> m_vars = new LinkedList();
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
    protected void contentReceived(String content, String tagname, Attributes m_tagattr){
        if("ok".equalsIgnoreCase(tagname)){
            m_val = content;
        }else if( "v".equalsIgnoreCase(tagname) ){
            m_vars.add(content.trim());
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
    
    public String prepareGetMessage(String sessionID, int lastMsgID){
        StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        return str.toString();
    }
    
    public String preparePutMessage(String sessionID){
        StringBuilder str = new StringBuilder();
        str.append( Config.getXMLfirstline() );
        return str.toString();
    }
}

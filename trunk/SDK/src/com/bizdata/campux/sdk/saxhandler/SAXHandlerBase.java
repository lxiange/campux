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
import com.bizdata.campux.sdk.Log;
import com.bizdata.campux.sdk.exception.NetworkErrorException;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * this class provides a base class and common functions for all SAX handlers
 * @author yuy
 */
public abstract class SAXHandlerBase extends DefaultHandler{
    public void parseInput(InputStream inputstream) throws Exception{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputstream, this);
        }catch(Exception exc){
            Log.log("UserStatus", Log.Type.NOTICE, exc);
            throw new NetworkErrorException(exc);
        }
    }
    
    protected boolean m_saxbody = false;
    protected boolean m_isOK = false;
    protected boolean m_b64 = false;
    protected String m_err_message = null;
    protected int m_err_code = -1;
    protected String m_tagname = null;
    protected String m_content = null;
    protected HashMap<String, String> m_moreatt = new HashMap<String, String>();
    protected final String[] f_moreattNames = {"t","u","d"};
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        m_content = "";
        if(Config.debug())
            System.out.println("StartElement:"+qName);
        m_saxbody = true;
        if( "ok".equalsIgnoreCase(qName) ){
            m_isOK = true;
            m_content = "";
        }else if( "err".equalsIgnoreCase(qName) ){
            m_isOK = false;
            m_err_code = Integer.parseInt(attributes.getValue("c"));
        }
        
        m_moreatt = new HashMap<String, String>();
        for(String name:f_moreattNames){
            String val = attributes.getValue(name);
            if( val!=null )
                m_moreatt.put(name, val);
        }
        
        m_tagname = qName;
        
        if( attributes.getValue("b64")!=null ){
            m_b64=true;
        }else{
            m_b64=false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if( !m_saxbody )
            return;
        if(Config.debug())
            System.out.println("Content:"+new String(ch, start, length)+" ok:"+m_isOK);
        if( m_isOK ){
            String content = new String(ch, start, length);
            m_content += content;
        }else{
            m_err_message = new String(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(m_saxbody){
            if( m_b64 ){
                byte[] bytes = DatatypeConverter.parseBase64Binary(m_content);
                m_content = new String(bytes, Config.getCharset());
            }
            contentReceived(m_content, m_tagname);
        }
        m_saxbody = false;
    }
    
    public boolean getIsError(){
        return !m_isOK;
    }
    public int getErrorCode(){
        return m_err_code;
    }
    public String getErrorMsg(){
        return m_err_message;
    }
    
    public void throwException() throws Exception{
        throw new Exception(m_err_message);
    }
    
    abstract protected void contentReceived(String content, String tagname);
    
}

package com.bizdata.campux.sdk.saxhandler;

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.sdk.Log;
import com.bizdata.campux.sdk.exception.NetworkErrorException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
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
    protected Attributes m_tagAttr = null;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(Config.debug())
            System.out.println("StartElement:"+qName);
        m_saxbody = true;
        if( "ok".equalsIgnoreCase(qName) ){
            m_isOK = true;
        }else if( "err".equalsIgnoreCase(qName) ){
            m_isOK = false;
            m_err_code = Integer.parseInt(attributes.getValue("c"));
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
            if( content!=null )
                    content = content.trim();
            if( m_b64 ){
                byte[] bytes = DatatypeConverter.parseBase64Binary(content);
                content = new String(bytes, Config.getCharset());
            }
            contentReceived(content, m_tagname, m_tagAttr);
        }else{
            m_err_message = new String(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
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
    
    abstract protected void contentReceived(String content, String tagname, Attributes m_tagattr);
    
}

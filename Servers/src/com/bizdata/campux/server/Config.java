/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author yuy
 */
public class Config {
    private static Config s_instance = null;
    
    private HashMap<String, List<String>> m_hash;
    
    private Config(){
        try{
            m_hash = new HashMap<String, List<String>>();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            
            SAXParser saxParser = factory.newSAXParser();
            //XMLReader saxParser = XMLReaderFactory.createXMLReader();

            DefaultHandler handler = new DefaultHandler() {
                String var, val;
                boolean root = true;

                @Override
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                    if( root ){
                        root = false;
                    }else{
                        var = qName;
                    }
                }
                
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    if( var==null )
                        return;
                    val = new String(ch, start, length).trim();
                    
                    List<String> list = m_hash.get(var);
                    if( list == null ){
                        list = new LinkedList();
                        m_hash.put(var, list);
                    }
                    list.add(val);
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    var = null;
                }
             };

            BufferedInputStream input = new BufferedInputStream(new FileInputStream(".config"));   
            //saxParser.setContentHandler(handler);
            //saxParser.parse(new InputSource(input));
            saxParser.parse(input, handler);
            input.close();
        }catch(Exception exc){
            Log.log("Config", Log.Type.FATAL, exc);
            System.exit(-1);
        }
    }
    
    static public List getValueSet(String name){
        if( s_instance==null )
            s_instance = new Config();
        return s_instance.m_hash.get(name);
    }
    static public String getValue(String name){
        if( s_instance==null )
            s_instance = new Config();
        List<String> v = s_instance.m_hash.get(name);
        if(v==null || v.size()==0)
            return null;
        return v.get(0);
    }
    static private String m_xmlcommonline = null;
    static public String getXMLfirstline(){
        if( m_xmlcommonline==null)
            m_xmlcommonline = "<?xml version=\"1.0\" encoding=\"" + getCharset().name() + "\" ?>";
        return m_xmlcommonline;
    }
    static private Charset m_commoncharset = null;
    static public Charset getCharset(){
        if( m_commoncharset == null )
            m_commoncharset = Charset.forName("UTF-8");
        return m_commoncharset;
    }
    static public boolean m_unittest = false;
    static public boolean isUnitTest(){
        return m_unittest;
    }
}

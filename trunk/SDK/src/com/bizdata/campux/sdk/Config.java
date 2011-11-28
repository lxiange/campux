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
package com.bizdata.campux.sdk;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
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
 * Config class is a singleton class that reads configuration XML file sdk.config
 * @author yuy
 */
public class Config {
    // singleton class
    private static Config s_instance = null;
    // the hash map used to store settings
    private HashMap<String, List<String>> m_hash;
    // when the class is instantialized, load the settings
    private Config(InputStream input){
        try{
            m_hash = new HashMap<String, List<String>>();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            
            SAXParser saxParser = factory.newSAXParser();

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

            //BufferedInputStream input = new BufferedInputStream(new FileInputStream("sdk.config"));   
            //saxParser.setContentHandler(handler);
            //saxParser.parse(new InputSource(input));
            saxParser.parse(input, handler);
            input.close();
        }catch(Exception exc){
            Log.log("Config", Log.Type.FATAL, exc);
            System.exit(-1);
        }
    }
    /**
     * set the input stream of the config file
     * @param input 
     */
    static public void init(InputStream input){
        if( s_instance==null ){
            s_instance = new Config(input);
        }
    }
    /**
     * get configuration values by name
     * @param name
     * @return a list of values with the name
     */
    static public List<String> getValueSet(String name){
        if( s_instance==null )
            return null;
        return s_instance.m_hash.get(name);
    }
    /**
     * get a configuration value by name. If many values with the same name, return the first one
     * @param name
     * @return 
     */
    static public String getValue(String name){
        if( s_instance==null )
            return null;
        List<String> v = s_instance.m_hash.get(name);
        if(v==null || v.size()==0)
            return null;
        return v.get(0);
    }
    
    // the first line of XML files
    static private String m_xmlcommonline = null;
    /**
     * get the first line of XML files
     * @return 
     */
    static public String getXMLfirstline(){
        if( m_xmlcommonline==null)
            m_xmlcommonline = "<?xml version=\"1.0\" encoding=\"" + getCharset().name() + "\" ?>";
        return m_xmlcommonline;
    }
    // the common charset used in this project
    static private Charset m_commoncharset = null;
    /**
     * get the common charset used in this project
     * @return 
     */
    static public Charset getCharset(){
        if( m_commoncharset == null )
            m_commoncharset = Charset.forName("UTF-8");
        return m_commoncharset;
    }
    /**
     * get if it is in the debug mode
     * @return true if in debug mode
     */
    static public boolean debug(){
        return true;
    }
}

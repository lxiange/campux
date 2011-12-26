package com.bizdata.campux.server.userinfo;
import com.bizdata.campux.server.*;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.cache.Cache;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class PublisherCache {
	private static PublisherCache s_instance;
	public static PublisherCache getInstance(){
        if( s_instance==null )
            s_instance = new PublisherCache();
        return s_instance;
    }
	private PublisherCache(){
        m_cachesize = Integer.parseInt(Config.getValue("PublisherCacheSize"));
        m_cache = new Cache(m_cachesize);
        m_variables = Config.getValueSet("PublisherVariable");
        m_path = Config.getValue("UserPublisherVariablePath");
    }
	// size of the number of users to cache
    protected int m_cachesize = 100;
    public int getCacheSize(){
        return m_cachesize;
    }
    // path to store files
    protected String m_path;
    
    protected Cache<String, Publisher> m_cache;
    protected List<String> m_variables;

    synchronized public String getPubliserVariable(String usr, String variable){
        Publisher pub = findPublisher(usr);
        if( pub==null )
            return null;
        return pub.m_values.get(variable);
    }
    public List<String> getVariables(){
        LinkedList<String> l = new LinkedList<String>(m_variables);
        return l;
    }
    synchronized public boolean setPublisherVariable(String usr, String state, String value){
    	Publisher pub = findPublisher(usr);
        HashSet<String> set = new HashSet<String>(m_variables);
        if( set.contains(state) ){
            pub.m_values.put(state, value);
            savePublisher(pub);
            return true;
        }
        return false;
    }
    protected Publisher findPublisher(String usr){
    	Publisher pub = m_cache.findItem(usr);
        
        if( pub==null){
            // user not found in cache, load from disk
        	pub = loadPublisher(usr);
            m_cache.cacheItem(usr, pub);
        }
        return pub;
    }
    protected Publisher loadPublisher(String usr){
        final Publisher pub = new Publisher();
        try{
        	pub.m_user = usr;
            File file = new File(m_path+usr+".state");
            if( !file.exists() ){
                savePublisher(pub);
            }
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                String var, val;
                boolean b64;
                boolean root = true;
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                    var = qName;
                    b64 = attributes.getValue("b64")==null ? false : true;                        
                }
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    if( var == null )
                        return;
                    val = new String(ch, start, length);
                    if( b64 ){
                        byte[] bytes = DatatypeConverter.parseBase64Binary(val);
                        val = new String(bytes, Charset.forName("UTF-8"));
                    }
                    pub.m_values.put(var, val);
                }
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    var = null;
                }
                
             };

            FileInputStream input = new FileInputStream(file);
            saxParser.parse(input, handler);
            input.close();
            
        }catch(Exception exc){
            Log.log("UserInfo", Type.FATAL, exc);
            return null;
        }
        return pub;
    }
    private void savePublisher(Publisher pub) {
        try{
            BufferedWriter output = new BufferedWriter( 
                    new OutputStreamWriter(new FileOutputStream(m_path + pub.m_user + ".publisher")));
            output.write(Config.getXMLfirstline() + "<root>");
            for(String v : m_variables){
                String val = pub.m_values.get(v);
                if( val==null ){
                    output.write("<"+ v +"></" + v + ">\n");
                }else{
                    byte[] valbytes = val.getBytes(Charset.forName("UTF-8"));
                    String val64 = DatatypeConverter.printBase64Binary(valbytes);
                    output.write("<" + v + " b64=\"true\">" + val64 + "</" + v + ">\n");
                }
            }
            output.write("</root>");
            output.close();
        }catch(Exception exc){
            Log.log("UserInfo", Type.FATAL, exc);
            return;
        }
    }
    
    
    class Publisher{
        public String m_user;
        public HashMap<String, String> m_values = new HashMap<String, String>();
    }
}

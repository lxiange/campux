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
        m_path = Config.getValue("UserPublisherPath");
    }
	// size of the number of users to cache
    protected int m_cachesize = 100;
    public int getCacheSize(){
        return m_cachesize;
    }
    // path to store files
    protected String m_path;
    
    protected Cache<String, Publisher> m_cache;

    protected LinkedList<Publisher> displayAllPublisher(){
    	LinkedList<Publisher> pubs=new LinkedList<Publisher>();
    	File dir=new File(m_path);
    	String[] filenames=dir.list();
    	LinkedList<String> pubnames=new LinkedList<String>();
    	for(String filename:filenames){
    		if(filename.endsWith(".publisher"))
    			pubnames.add(filename.substring(0, filename.indexOf(".publisher")));
    	}
    	for(String usr:pubnames)
    	    pubs.add(findPublisher(usr));
    	return pubs;
    }
    synchronized public String getPublisherIcon(String usr){
        Publisher pub = findPublisher(usr);
        if( pub==null )
            return null;
        return pub.p_iconname;
    }
    synchronized public String getPublisherDisplayName(String usr){
        Publisher pub = findPublisher(usr);
        if( pub==null )
            return null;
        return pub.p_displayname;
    }
    synchronized public List<String> getPublisherType(String usr){
        Publisher pub = findPublisher(usr);
        if( pub==null )
            return null;
        return pub.p_infotype;
    }
    
    synchronized public boolean setPublisherIcon(String usr,String icon){
    	Publisher pub = findPublisher(usr);
    	if(pub==null)
    		return false;
    	pub.p_iconname=icon;
        savePublisher(pub);
        return true;
    }
    synchronized public boolean setPublisherDisplayName(String usr,String displayname){
    	Publisher pub = findPublisher(usr);
    	if(pub==null)
    		return false;
    	pub.p_displayname=displayname;
        savePublisher(pub);
        return true;
    }
    synchronized public boolean addPublisherInfoType(String usr,String infotype){
    	Publisher pub = findPublisher(usr);
    	if(pub==null)
    		return false;
    	for(String v:pub.p_infotype){
    		if(v.equalsIgnoreCase(infotype))
    			return false;
    	}
    	pub.p_infotype.add(infotype);
    	return true;
    }
    synchronized public boolean deletePublisherInfoType(String usr,String infotype){
    	Publisher pub = findPublisher(usr);
    	if(pub==null)
    		return false;
    	for(String v:pub.p_infotype){
    		if(v.equalsIgnoreCase(infotype))
    			pub.p_infotype.remove(v);
    		return true;
    	}
    	return false;
    }
    
    
    protected boolean havePublisher(String usr){
    	LinkedList<Publisher> pubs=displayAllPublisher();
    	for(Publisher pub:pubs){
    		if(pub.m_user.equalsIgnoreCase(usr)){
    			return true;
    		}
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
            File file = new File(m_path+usr+".publisher");
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
                    if("ip".equalsIgnoreCase(var)){
                    	pub.p_iconname=val;
                    }else if("dp".equalsIgnoreCase(var)){
                    	pub.p_displayname=val;
                    }else if("cp".equalsIgnoreCase(var)){
                    	pub.p_infotype.add(val);
                    }
                }
                @Override
                public void endElement(String uri, String localName, String qName)throws SAXException {
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
            output.write(Config.getXMLfirstline() + "<rp>");
            output.write("<ip b64=\"true\">"+pub.p_iconname+"</ip>");
            output.write("<dp>"+pub.p_displayname+"</dp>");
            for(String v : pub.p_infotype){
                String val = v;
                output.write("<cp>"+val+"</cp>");
            }
            output.write("</rp>");
            output.close();
        }catch(Exception exc){
            Log.log("UserInfo", Type.FATAL, exc);
            return;
        }
    }
    
    synchronized public boolean registerNewPublisher(Publisher pub){
    	if(havePublisher(pub.m_user))
    		return false;
    	else{
    		savePublisher(pub);
    		return true;
    	}
    }
}
class Publisher{
    public String m_user;
    public String p_iconname;
    public String p_displayname;
    public LinkedList<String> p_infotype=new LinkedList<String>();
}
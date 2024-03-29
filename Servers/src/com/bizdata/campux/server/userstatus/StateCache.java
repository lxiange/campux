/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.userstatus;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.cache.Cache;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import org.xml.sax.Attributes;

/**
 *
 * @author yuy
 */
public class StateCache {
    
    private static StateCache s_instance;
    
    public static StateCache getInstance(){
        if( s_instance==null )
            s_instance = new StateCache();
        return s_instance;
    }
    
    private StateCache(){
        m_cachesize = Integer.parseInt(Config.getValue("StateCacheSize"));
        m_cache = new Cache(m_cachesize);
        m_variables = Config.getValueSet("StateVariable");
        m_path = Config.getValue("UserStateVariablePath");
        File file = new File(m_path);
        if( !file.exists() || !file.isDirectory() ){
            if( !file.mkdirs() )
                Log.log("UserStateCache", Type.FATAL, "directory can not be created:" + m_path);
        }
    }
    
    // size of the number of users to cache
    protected int m_cachesize = 100;
    public int getCacheSize(){
        return m_cachesize;
    }
    // path to store files
    protected String m_path;
    
    protected Cache<String, State> m_cache;
    protected List<String> m_variables;
    
    synchronized public String getUserState(String usr, String state){
        State userstate = findUser(usr);
        if( userstate==null )
            return null;
        return userstate.m_values.get(state);
    }
    synchronized public boolean setUserState(String usr, String state, String value){
        State userstate = findUser(usr);
        HashSet<String> set = new HashSet<String>(m_variables);
        if( set.contains(state) ){
            userstate.m_values.put(state, value);
            saveUser(userstate);
            return true;
        }
        return false;
    }
    public List<String> getVariables(){
        LinkedList<String> l = new LinkedList<String>(m_variables);
        return l;
    }
    
    protected State findUser(String usr){
        State userstate = m_cache.findItem(usr);
        
        if( userstate==null){
            // user not found in cache, load from disk
            userstate = loadUser(usr);
            m_cache.cacheItem(usr, userstate);
        }
        
        return userstate;
    }
    
    // read user state from file
    protected State loadUser(String usr){
        final State state = new State();
        try{
            state.m_user = usr;
            File file = new File(m_path+usr+".state");
            if( !file.exists() ){
                saveUser(state);
            }
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                String var, val;
                boolean b64;
                boolean root = true;
                String content = null;
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                    var = qName;
                    b64 = attributes.getValue("b64")==null ? false : true;                        
                    content = "";
                }
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    if( var == null )
                        return;
                    val = new String(ch, start, length);
                    content = content + val;
                }
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if( b64 ){
                        byte[] bytes = DatatypeConverter.parseBase64Binary(content);
                        val = new String(bytes, Charset.forName("UTF-8"));
                    }
                    state.m_values.put(var, val);

                    var = null;
                }
                
             };

            FileInputStream input = new FileInputStream(file);
            saxParser.parse(input, handler);
            input.close();
            
        }catch(Exception exc){
            Log.log("UserProfile", Type.FATAL, exc);
            return null;
        }
        return state;
    }

    // save user state to file
    private void saveUser(State userstate) {
        try{
            BufferedWriter output = new BufferedWriter( 
                    new OutputStreamWriter(new FileOutputStream(m_path + userstate.m_user + ".state")));
            output.write(Config.getXMLfirstline() + "<root>");
            for(String v : m_variables){
                String val = userstate.m_values.get(v);
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
            Log.log("UserStateCache", Type.FATAL, exc);
            return;
        }
    }
    
    class State{
        public String m_user;
        public HashMap<String, String> m_values = new HashMap<String, String>();
    }
}

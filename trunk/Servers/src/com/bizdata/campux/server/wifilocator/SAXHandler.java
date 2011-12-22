package com.bizdata.campux.server.wifilocator;

import com.bizdata.campux.sdk.User;
import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.SAXHandlerBase;
import com.bizdata.campux.server.exception.ParseEndException;
import com.bizdata.intellix.wifilocating.LocationItem;
import com.bizdata.intellix.wifilocating.Locator;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handle the received commands in xml format through SAX
 * @author yuy
 */
public class SAXHandler extends SAXHandlerBase{
    // defining commands
    enum Command{
        GET_LOCATION(110,"g"), 
        ADD_LOCATION(120,"a");
        int m_state;
        String m_string;
        Command(int state, String string){
            m_state = state;
            m_string = string;
        }
        public String string() {return m_string;}
        public int state(){return m_state;}
    }
    
    // state of the current system
    int m_state = 0;    
    // store temporary attributes for parsing XML
    Attributes m_attr;
    // store temporary content for parsing XML
    String m_content;
    // store tagname
    String m_tagname;
    // store users publish to
    LocationItem m_item;
    // store the message
    String m_usersession;
    
    /**
     * 处理XML的一个项目的开头
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException 
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(m_state==0){
            m_item = new LocationItem();
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_state = cmd.state();
                    m_attr = attributes;
                    m_content = null;
                    m_usersession = m_attr.getValue("s");
                    m_item.name = m_attr.getValue("l");
                    break;
                }
            }
        }else{
            m_attr = attributes;
            m_tagname = qName;
        }
    }
    /**
     * 处理XML文件中的非开始和终止记号
     * @param ch
     * @param start
     * @param length
     * @throws SAXException 
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if( m_state<=0)
            return; //忽略以外字符
        m_content = new String(ch, start, length).trim();
        
        if( "i".equalsIgnoreCase(m_tagname)){
            m_item.ip = m_content;
        }else if( "w".equalsIgnoreCase(m_tagname) ){
            String bssid = m_content;
            Integer s = Integer.parseInt(m_attr.getValue("s"));
            if( m_attr.getValue("c")!=null )
                m_item.connectedWifi = bssid;
            m_item.wifis.put(bssid, s);
        }
    }
    /**
     * 处理XML文件的项目终止记号
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException 
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if( m_state==0 ){
            //not needed
        }else{
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_state = 0;
                    fire(cmd);
                    break;
                }
            }
        }
        // clean up
        m_tagname = null;
        m_content = null;
        m_attr = null;
    }

    /**
     * 文档结束，清理变量
     * @throws SAXException 
     */
    @Override
    public void endDocument() throws SAXException {
        m_tagname = null;
        m_content = null;
        m_attr = null;
    }    
    
    /**
     * function that maps the commands into corresponding functions
     * @param cmd 
     */
    protected void fire(Command cmd) {
        try{
            System.out.println("fire:"+cmd);
            switch(cmd){
                case GET_LOCATION: 
                    func_GET_LOCATION();
                    break;
                case ADD_LOCATION:
                    func_ADD_LOCATION();
                    break;
            }
        }catch(Exception exc){
            responseError(0, exc.getMessage());
        }
    }
    /**
     * function for list system variables
     */
    protected void func_GET_LOCATION(){
        //User userauth = new User();
        String user = null; //userauth.lookupUsername(m_usersession); // go get user id;
        
        Log.log("UserStatusServer", Type.INFO, "read for: " + m_usersession + ":"+ user);
        /*if( user==null ){
            responseError(0, "user not valid");
            return;
        }*/
        
        StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok b64=\"true\">");
        try{
            Locator locator = Locator.getInstance();
            String lname = locator.locate(m_item.wifis);
            lname = DatatypeConverter.printBase64Binary(lname.getBytes(Config.getCharset()));
            strbuilder.append(lname);
            strbuilder.append("</ok>");
            response(strbuilder.toString());
        }catch(Exception exc){
            Log.log("wifi", Type.ERROR, exc);
            responseError(0, "error getting location: " + exc.getMessage());
        }
    }
    /**
     * function for read system variables, once at a time
     * @param attr
     * @param content 
     */
    protected void func_ADD_LOCATION(){
        //User userauth = new User();
        String user = null;//userauth.lookupUsername(m_usersession); // go get user id;
        
        Log.log("UserStatusServer", Type.INFO, "read for: " + m_usersession + ":"+ user);
        /*if( user==null ){
            responseError(0, "user not valid");
            return;
        }*/
        
        try{
            Locator locator = Locator.getInstance();
            locator.addData(m_item);
        }catch(Exception exc){
            Log.log("wifi", Type.ERROR, exc);
            responseError(0, "error getting location: " + exc.getMessage());
        }
        response("<ok></ok>");
    }
}
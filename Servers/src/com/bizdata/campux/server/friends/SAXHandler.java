/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.friends;

import com.bizdata.campux.sdk.User;
import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.SAXHandlerBase;
import com.bizdata.campux.server.exception.ParseEndException;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import com.bizdata.campux.server.cache.Piece;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author yuy
 * @date 2012-02-12 10:12:53
 */
public class SAXHandler extends SAXHandlerBase{
    // defining commands
    enum Command{
        READ(110,"rs"), 
        PUBLISH(120,"ps");
        int m_state;
        String m_string;
        Command(int state, String string){
            m_state = state;
            m_string = string;
        }
        public String string() {return m_string;}
        public int state(){return m_state;}
    }
    final String f_servername = "FriendServer";
    
    // the command
    Command m_cmd;
    // state of the current system
    int m_state = 0;
    // store temporary content for parsing XML
    String m_content;
    // store tagname
    String m_tagname;
    // store users publish to
    boolean m_b64 = false;
    // store the message
    String m_usersession;
    // date for update
    String m_date;
    
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
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_cmd = cmd;
                    m_state = cmd.state();
                    m_usersession = attributes.getValue("s");
                    m_date = attributes.getValue("d");
                    String tmp = attributes.getValue("b64");
                    if( tmp!=null )
                        m_b64=true;
                    break;
                }
            }
        }else{
            // no code here
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
        if( m_b64 ){
            byte[] bytes = DatatypeConverter.parseBase64Binary(m_content);
            m_content = new String(bytes, Config.getCharset());
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
            if( m_cmd.string().equalsIgnoreCase(qName) ){
                m_state = 0;
                fire(m_cmd);
                throw new ParseEndException();
            }
        }
    }

    /**
     * 文档结束，清理变量
     * @throws SAXException 
     */
    @Override
    public void endDocument() throws SAXException {
        m_tagname = null;
        m_content = null;
        m_usersession = null;
        m_cmd = null;
        m_date = null;
    }    
    
    /**
     * function that maps the commands into corresponding functions
     * @param cmd 
     */
    protected void fire(Command cmd) {
        try{
            System.out.println("fire:"+cmd);
            switch(cmd){
                case READ: 
                    func_READ();
                    break;
                case PUBLISH:
                    func_PUBLISH();
                    break;
            }
        }catch(Exception exc){
            responseError(0, exc.getMessage());
        }
    }
    /**
     * function for reading messages
     */
    protected void func_READ(){
        User user = new User();
        User sysuser = new User();
        StringBuilder str = new StringBuilder();
        
        String username = Config.isUnitTest() ? m_usersession : 
                user.lookupUsername(m_usersession); // go get user id;
        
        Log.log(f_servername, Type.INFO, "read for: " + m_usersession + ":"+ username);
        /*if( user==null ){
            responseError(0, "user not valid");
            return;
        }*/
        
        str.append("<ok>");
        try{
            sysuser.login(Config.getValue("Service_User"), Config.getValue("Service_Psw"));
            List<String> friends = sysuser.friendRead(username);
            String fristr = "";
            if( friends!=null )
                for(String tmp:friends)
                    fristr += tmp+";";
            
            Long timeafter = 0L;
            if( m_date!=null )
                timeafter = Long.parseLong(m_date);
            
            Log.log(f_servername, Type.INFO, "friends: " + fristr + " and time:"+ timeafter);
        
            for(String friend: friends){
                List<Piece> list = FriendRooms.roomModel().readPieces(friend);
                if( list == null )
                    continue;
                for(Piece p:list){
                    if( p.m_timestamp <= timeafter )
                        continue;
                    String msg64 = DatatypeConverter.printBase64Binary(p.m_content.getBytes(Config.getCharset()));

                    str.append("<m d=\"" + (long)(p.m_timestamp));
                    str.append("\" b64=\"true\" u=\"");
                    str.append(friend);
                    str.append("\">");
                    str.append(msg64);
                    str.append("</m>");
                }
            }
        }catch(Exception exc){
            Log.log(f_servername, Type.ERROR, exc);
            responseError(0, "error analyzing document: " + exc.getMessage());
            return;
        }
        
        str.append("</ok>");
        response(str.toString());
    }
    /**
     * function for publishing messages
     * @param attr
     * @param content 
     */
    protected void func_PUBLISH(){
        User user = new User();
        String username = Config.isUnitTest() ? 
                m_usersession : user.lookupUsername(m_usersession); // go get user id;
        
        Log.log(f_servername, Type.INFO, "write for: " + m_usersession + ":"+ username);
        /*if( user==null ){
            responseError(0, "user not valid");
            return;
        }*/
        
        String message = m_content;
        try{
            Log.log(f_servername, Type.INFO, "message:"+ message);
        
            FriendRooms.roomModel().appendPiece(username, message);
        }catch(Exception exc){
            Log.log(f_servername, Type.ERROR, exc);
            responseError(0, "error publish message: " + exc.getMessage());
        }
        response("<ok></ok>");
    }
}
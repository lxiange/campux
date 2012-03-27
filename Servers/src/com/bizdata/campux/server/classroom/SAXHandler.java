/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.classroom;

import com.bizdata.campux.sdk.Friend;
import com.bizdata.campux.sdk.User;
import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.SAXHandlerBase;
import com.bizdata.campux.server.exception.ParseEndException;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import com.bizdata.campux.server.cache.Piece;
import java.util.LinkedList;
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
        LIST_CLASSROOM(110,"lc"), 
        PUBLISH_INFO(120,"pr"),
        READ_INFO(130,"rr");
        int m_state;
        String m_string;
        Command(int state, String string){
            m_state = state;
            m_string = string;
        }
        public String string() {return m_string;}
        public int state(){return m_state;}
    }
    final String f_servername = "ClassRoomServer";
    
    // the command
    Command m_cmd;
    // state of the current system
    int m_state = 0;
    // store temporary content for parsing XML
    String m_content;
    // store users publish to
    boolean m_b64 = false;
    // store the message
    String m_usersession;
    // location
    String m_comment;
    
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
        m_content = "";
        if(m_state==0){
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_cmd = cmd;
                    m_state = cmd.state();
                    m_usersession = attributes.getValue("s");
                    m_comment = attributes.getValue("c");
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
        m_content += new String(ch, start, length);
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
            if( m_b64 ){
                byte[] bytes = DatatypeConverter.parseBase64Binary(m_content);
                m_content = new String(bytes, Config.getCharset());
            }
            
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
        m_content = null;
        m_usersession = null;
        m_cmd = null;
    }    
    
    /**
     * function that maps the commands into corresponding functions
     * @param cmd 
     */
    protected void fire(Command cmd) {
        try{
            System.out.println("fire:"+cmd);
            switch(cmd){
                case LIST_CLASSROOM: 
                    func_LIST_CLASSROOM();
                    break;
                case PUBLISH_INFO:
                    func_PUBLISH_INFO();
                    break;
                case READ_INFO:
                    func_READ_INFO();
            }
        }catch(Exception exc){
            responseError(0, exc.getMessage());
        }
    }
    /**
     * function for reading classroom list
     */
    protected void func_LIST_CLASSROOM(){
        Log.log(f_servername, Type.INFO, "list classrooms");
        
        StringBuilder str = new StringBuilder();
        str.append("<ok>");
        List<String> lists = ClassRooms.roomModel().listRooms();
        for(String room : lists){
            str.append("<c b64=\"true\" m=\"");
            str.append(calcClassRoomScore(room));
            str.append("\">");
            //Log.log(f_servername, Type.INFO, "   " + room);
            String msg64 = DatatypeConverter.printBase64Binary(room.getBytes(Config.getCharset()));
            str.append(msg64);
            str.append("</c>");
        }
        str.append("</ok>");
        response(str.toString());
    }
    /**
     * function for publish classroom info
     */
    protected void func_PUBLISH_INFO(){
        User user = new User();
        String username = Config.isUnitTest() ? m_usersession : 
                user.lookupUsername(m_usersession); // go get user id;
        if( user==null ){
            responseError(0, "user not valid");
            return;
        }
        
        Log.log(f_servername, Type.INFO, "publish for: " + m_usersession + ":"+ username 
                + " location:" + m_content + " comment:" + m_comment);
        
        if( m_content==null || m_content.isEmpty() ){
            Log.log(f_servername, Type.INFO, "no location provided.");
            responseError(1001, "no location provided.");
            return;
        }
        if( !m_comment.equals("0") && !m_comment.equals("1")){
            Log.log(f_servername, Type.INFO, "bad content format.");
            responseError(1002, "bad content format.");
            return;
        }
        
        boolean success = ClassRooms.roomModel().appendPiece(m_content, m_comment);
        if( !success ){
            Log.log(f_servername, Type.INFO, "failed to publish a classroom comment.");
            responseError(1003, "failed to publish a classroom comment.");
            return;
        }
        response("<ok></ok>");
    }
    
    protected void func_READ_INFO(){
        User user = new User();
        String username = Config.isUnitTest() ? m_usersession : 
                user.lookupUsername(m_usersession); // go get user id;
        
        if( user==null ){
            responseError(0, "user not valid");
            return;
        }
        
        Log.log(f_servername, Type.INFO, "read for: " + m_usersession + ":"+ username + " location:" + m_content);
        
        int score = calcClassRoomScore(m_content);
        
        if( score>=0 )
            response("<ok>" + score + "</ok>");
        return;
    }
    
    private int calcClassRoomScore(String roomname){
        List<Piece> pieces = ClassRooms.roomModel().readPieces(roomname);
        if( pieces==null )
            return -1;
        
        boolean pre = ClassRooms.isPrearranged(roomname);
                
        int count1=0, count0=0;
        for(Piece p:pieces){
            if( "1".equals(p.m_content) ){
                count1 ++;
            }else if( "0".equals(p.m_content) ){
                count0 ++;
            }
        }
        if( pre )
            count0 += 20;
        else
            count1 += 15;
        double delta = (count1-count0)/5.0;
        double score = 100.0/(1+Math.exp(-delta));
        return (int)(Math.round(score));
    }
}
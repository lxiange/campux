package com.bizdata.campux.server.userstatus;

import com.bizdata.campux.sdk.User;
import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.SAXHandlerBase;
import com.bizdata.campux.server.exception.ParseEndException;
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
        LIST_SYS_VARIABLES(110,"usl"), 
        READ_SYS_VARIABLE(120,"usr"), 
        WRITE_SYS_VAIRABLE(130,"usw"),
        UPDATE_MSGBOX(210,"mg"),
        PUBLISH_MSGBOX(220,"ms"),
        DELETE_MSGBOX(230,"md");
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
    List<String> m_users;
    // store groups publish to
    List<String> m_groups;
    // store the message
    String m_message;
    
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
                    m_state = cmd.state();
                    m_attr = attributes;
                    m_content = null;
                    break;
                }
            }
            if( m_state == Command.PUBLISH_MSGBOX.state() ){
                m_users = new LinkedList<String>();
                m_groups = new LinkedList<String>();
            }
        }else{
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
        
        if( "u".equalsIgnoreCase(m_tagname)){
            m_users.add(m_content);
        }else if( "g".equalsIgnoreCase(m_tagname) ){
            m_groups.add(m_content);
        }else if( "m".equalsIgnoreCase(m_tagname) ){
            m_message = m_content;
            if( m_attr.getValue("b64")!=null ){
                byte[] bytes = DatatypeConverter.parseBase64Binary(m_message);
                m_message = new String(bytes, Config.getCharset());
            }
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
        m_groups = null;
        m_users = null;
    }    
    
    /**
     * function that maps the commands into corresponding functions
     * @param cmd 
     */
    protected void fire(Command cmd) {
        try{
            System.out.println("fire:"+cmd);
            switch(cmd){
                case LIST_SYS_VARIABLES: 
                    func_LIST_SYS_VARIABLES();
                    break;
                case READ_SYS_VARIABLE:
                    func_READ_SYS_VARIABLE(m_attr, m_content);
                    break;
                case WRITE_SYS_VAIRABLE: 
                    func_WRITE_SYS_VAIRABLE(m_attr, m_content);
                    break;
                case UPDATE_MSGBOX:
                    func_UPDATE_MSGBOX(m_attr, m_content);
                    break;
                case PUBLISH_MSGBOX:
                    func_PUBLISH_MSGBOX(m_attr, m_users, m_groups, m_message);
                    break;
                case DELETE_MSGBOX:
                    func_DELETE_MSGBOX(m_attr, m_content);
                    break;
            }
        }catch(Exception exc){
            responseError(0, exc.getMessage());
        }
    }
    /**
     * function for list system variables
     */
    protected void func_LIST_SYS_VARIABLES(){
        StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok>");
        
        List<String> list = StateCache.getInstance().getVariables();
        for(String v:list){
            strbuilder.append("<v>");
            strbuilder.append(v);
            strbuilder.append("</v>");
        }
        
        strbuilder.append("</ok>");
        response(strbuilder.toString());
    }
    /**
     * function for read system variables, once at a time
     * @param attr
     * @param content 
     */
    protected void func_READ_SYS_VARIABLE(Attributes attr, String content){
        StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok b64=\"true\">");
        
        String usd = attr.getValue("s");
        
        User userauth = new User();
        String user = userauth.lookupUsername(usd); // go get user id;
        
        Log.log("UserStatusServer", Type.INFO, "read for: " + usd + ":"+ user);
        
        String varname = content;
        String val = StateCache.getInstance().getUserState(user, varname);
        
        val = val == null ? null : DatatypeConverter.printBase64Binary(val.getBytes(Config.getCharset()));
        
        strbuilder.append(val==null?"":val);
        strbuilder.append("</ok>");
        
        response(strbuilder.toString());
    }
    /**
     * function for write system variables, once at a time
     * @param attr
     * @param content 
     */
    protected void func_WRITE_SYS_VAIRABLE(Attributes attr, String content){
        // 从属性s的用户sessionID查询用户名称
        String usd = attr.getValue("s");
        User userauth = new User();
        String user = userauth.lookupUsername(usd);        
        // 从属性n获得变量名称
        String varname = attr.getValue("n");
        // 从属性b64获得内容是否是base64编码
        String b64 = attr.getValue("b64");
        // 内容
        String val = content;
        // 解base64码
        if( b64!=null ){
            byte[] bytes = DatatypeConverter.parseBase64Binary(val);
            val = new String(bytes);
        }
        
        Log.log("UserStatusServer", Type.INFO, "write for: " + usd + ":"+ user+ " " + varname+":"+content+" " + b64+" " +val);
        //从Cache中取出用户变量
        if( !StateCache.getInstance().setUserState(user, varname, val) ){
            responseError(151,"No such system variable");
        }else{        
            response("<ok></ok>");
        }
    }
    /***	获取MessageBox更新
		请求：<mg s="用户会话编号">起始ID<mg>
		OK响应（有更新）：<ok>
		    <b u="发布人" t="发布时间" d="消息id" b64="true">消息1</b>
		    <b u="发布人" t="发布时间" d="消息id" b64="true">消息2</b>
		    </ok>
		OK响应（无更新）：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
     * */
    protected void func_UPDATE_MSGBOX(Attributes attr, String content) {
        // 从属性s的用户sessionID查询用户名称
        String usd = attr.getValue("s");
        User userauth = new User();
        String user = userauth.lookupUsername(usd);
        // id
        int msgid = Integer.parseInt(content);
        
        UserMessage usermsg = UserMessage.getInstance();
        
        Message[] msgs = usermsg.getNewMessage(user, msgid);
        
        StringBuilder str = new StringBuilder();
        str.append("<ok>");
        for(Message msg:msgs){
            str.append("<b u=\"");
            str.append(msg.publisher);
            str.append("\" t=\"");
            str.append(msg.date);
            str.append("\" d=\"");
            str.append(msg.id);
            str.append("\" b64=\"true\">");
            String b64 = DatatypeConverter.printBase64Binary(msg.message.getBytes(Config.getCharset()));
            str.append(b64);
            str.append("</b>");
        }
        str.append("</ok>");
        response(str.toString());
    }
    /***	发布MessageBox消息
		请求：<ms s="用户会话编号"><u>发布对象用户</u><g>发布对象组</g><m b64="true">消息</m><ms>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
     */
    protected void func_PUBLISH_MSGBOX(Attributes attr, List<String> users, List<String> groups, String msg) throws Exception{
        String usd = attr.getValue("s");
        User userauth = new User();
        String user = userauth.lookupUsername(usd);
        String[] ugs = userauth.userGroups(user);
        boolean passcheck = false;
        for(String ug:ugs){
            if( "admin".equalsIgnoreCase(ug) || "system".equalsIgnoreCase(ug) || "verifiedapp".equalsIgnoreCase(ug) ){
                passcheck = true;
                break;
            }
        }
        
        if( !passcheck )
            throw new Exception("Unauthorized action");
        
        LinkedList<String> targetusers = new LinkedList<String>(users);
        for(String group:groups){
            String[] groupusers = userauth.groupUsers(group);
            for(String groupuser : groupusers){
                targetusers.add(groupuser);
            }
        }
        
        Message msgobj = new Message();
        msgobj.publisher = user;
        msgobj.message = msg;
        DateFormat format = DateFormat.getDateTimeInstance();
        msgobj.date = format.format(Calendar.getInstance().getTime());
        
        UserMessage.getInstance().putMessage(msgobj, targetusers);
        
        response("<ok></ok>");
    }
    /**	删除Message
		请求：<md s="用户会话编号">消息ID<md>
		OK响应：<ok></ok>
		失败响应：<err c="错误代码">错误原因</err>
         * */
    protected void func_DELETE_MSGBOX(Attributes attr, String idstr){
        String usd = attr.getValue("s");
        User userauth = new User();
        String user = userauth.lookupUsername(usd);
        
        int id = Integer.parseInt(idstr);
        
        UserMessage.getInstance().deleteMessage(user, id);
        
        response("<ok></ok>");
    }
}

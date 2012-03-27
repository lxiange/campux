package com.bizdata.campux.server.userstatus;

import com.bizdata.campux.sdk.Friend;
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
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import javax.imageio.ImageIO;
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
    protected final String f_servername = "UserStatusServer";
    // the command
    Command m_cmd;
    // state of the current system
    int m_state = 0;    
    // store temporary content for parsing XML
    StringBuilder m_content = new StringBuilder();
    // store tagname
    String m_tagname;
    // user session
    String m_usersession;
    // target user
    String m_targetuser;
    // base64 encoding
    String m_b64=null;
    // variable name
    String m_varname;
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
        m_content=new StringBuilder();
        if(m_state==0){
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_cmd = cmd;
                    m_state = cmd.state();
                    m_usersession = attributes.getValue("s");
                    m_targetuser = attributes.getValue("u");
                    m_b64 = attributes.getValue("b64");
                    m_varname = attributes.getValue("n");
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
        if( length>0 )
            m_content.append(ch, start, length);
        System.out.println("characters: " + m_content.toString());
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
            if( "u".equalsIgnoreCase(m_tagname)){
                m_users.add(m_content.toString());
            }else if( "g".equalsIgnoreCase(m_tagname) ){
                m_groups.add(m_content.toString());
            }else if( "m".equalsIgnoreCase(m_tagname) ){
                m_message = m_content.toString();
                if( m_b64!=null ){
                    byte[] bytes = DatatypeConverter.parseBase64Binary(m_message);
                    m_message = new String(bytes, Config.getCharset());
                }
            }
            
            if( m_cmd.string().equalsIgnoreCase(qName) ){
                System.out.println("fire with: " + m_content.toString());
                m_state = 0;
                fire(m_cmd);
                throw new ParseEndException();
            }
        }
        // clean up
        m_tagname = null;
        m_content = null;
    }

    /**
     * 文档结束，清理变量
     * @throws SAXException 
     */
    @Override
    public void endDocument() throws SAXException {
        m_tagname = null;
        m_content = null;
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
                    func_READ_SYS_VARIABLE(m_content.toString());
                    break;
                case WRITE_SYS_VAIRABLE: 
                    func_WRITE_SYS_VAIRABLE(m_content.toString());
                    break;
                case UPDATE_MSGBOX:
                    func_UPDATE_MSGBOX(m_content.toString());
                    break;
                case PUBLISH_MSGBOX:
                    func_PUBLISH_MSGBOX(m_users, m_groups, m_message);
                    break;
                case DELETE_MSGBOX:
                    func_DELETE_MSGBOX(m_content.toString());
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
    protected void func_READ_SYS_VARIABLE(String content){
        StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok b64=\"true\">");
        // 从属性s的用户sessionID查询用户名称
        String usd = m_usersession;
        User userauth = new User();
        String user = userauth.lookupUsername(usd); // go get user id;
        // 从属性u得到要查询的目标用户名称
        String targetuser = m_targetuser;
        // 若被忽略，则查询自己
        if( targetuser==null || targetuser.isEmpty())
            targetuser = user;
        
        Log.log(f_servername, Type.INFO, "read for: " + usd + ":"+ user + " of user:" + targetuser);
        
        String varname = content;
        String val = StateCache.getInstance().getUserState(targetuser, varname);
        
        Log.log(f_servername, Type.INFO, "read out: " + varname + "="+ val);
        // 进行编码
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
    protected void func_WRITE_SYS_VAIRABLE(String content){
        // 从属性s的用户sessionID查询用户名称
        String usd = m_usersession;
        User userauth = new User();
        String user = userauth.lookupUsername(usd);  
        // 从属性u获得目标用户名称
        String targetuser = m_targetuser;
        // 默认为自己
        if( targetuser==null || targetuser.isEmpty() )
            targetuser = user;
        // 检查权限
        boolean isAuthorized = false;
        if( targetuser == user || targetuser.equals(user) ){
            isAuthorized = true;
        }else{
            try{
                userauth.login(Config.getValue("Service_User"), Config.getValue("Service_Psw"));
                String[] groups = userauth.userGroups(user);
                for(String group : groups)
                    if( group.equalsIgnoreCase("system") || group.equalsIgnoreCase("admin")){
                        isAuthorized = true;
                        break;
                    }
            }catch(Exception exc){
                Log.log(f_servername, Type.INFO, exc);
            }
        }
        // 普通用户不允许写别人
        if( !isAuthorized ){
            responseError(150, "unauthorized access to user variables");
            return;
        }
            
        // 从属性n获得变量名称
        String varname = m_varname;
        // 从属性b64获得内容是否是base64编码
        //String b64 = attr.getValue("b64");
        // 内容
        String val = content;
        // 解base64码
        if( m_b64!=null ){
            byte[] bytes = DatatypeConverter.parseBase64Binary(val);
            val = new String(bytes, Config.getCharset());
        }
        
        Log.log(f_servername, Type.INFO, "write for: " + usd + ":"+ user+ " to user:" + targetuser + " " + varname);
        //从Cache中取出用户变量
        String oldval = StateCache.getInstance().getUserState(targetuser, varname);
        if( !StateCache.getInstance().setUserState(targetuser, varname, val) ){
            responseError(151,"No such system variable");
        }else{
            if( "UserStatus".equals(varname) ){
                if( val!=null && !val.equals(oldval)){
                    boolean suc = user_info_updated(targetuser, 0, val);
                    if(!suc) responseError(152,"Failed to publish status change");
                }
            }else if( "UserLocation".equals(varname) ){
                if( val!=null && !val.equals(oldval)){
                    boolean suc = user_info_updated(targetuser, 1, val);
                    if(!suc) responseError(152,"Failed to publish location change");
                }
            }else if( "UserPhoto".equals(varname) ){
                scale_photo(targetuser, val);
            }
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
    protected void func_UPDATE_MSGBOX(String content) {
        // 从属性s的用户sessionID查询用户名称
        String usd = m_usersession;
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
    protected void func_PUBLISH_MSGBOX(List<String> users, List<String> groups, String msg) throws Exception{
        String usd = m_usersession;
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
    protected void func_DELETE_MSGBOX(String idstr){
        String usd = m_usersession;
        User userauth = new User();
        String user = userauth.lookupUsername(usd);
        
        int id = Integer.parseInt(idstr);
        
        UserMessage.getInstance().deleteMessage(user, id);
        
        response("<ok></ok>");
    }
    
    protected boolean user_info_updated(String targetuser, int type, String content){
        User user = new User();
        try{
            user.login(Config.getValue("Service_User"), Config.getValue("Service_Psw"));
            Friend fobj = new Friend(user);
            fobj.__friendStatusPublish(targetuser, type+content);
        }catch(Exception exc){
            Log.log(f_servername, Type.NOTICE, exc);
            return false;
        }
        return true;
    }
    
    private void scale_photo(String targetuser, String val){
        if( val==null || val.isEmpty() )
            return;
        try {
            byte[] imgbytes = DatatypeConverter.parseBase64Binary(val);
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(imgbytes)); // 读入文件  
            int width = src.getWidth(); // 得到源图宽  
            int height = src.getHeight(); // 得到源图长 
            int maxedge = width > height? width : height;
            double ratio = 70.0/(double)maxedge;
            int afterwidth = (int)(Math.round(ratio*width));
            int afterheight = (int)(Math.round(ratio*height));
                            
            Image image = src.getScaledInstance(afterwidth, afterheight, Image.SCALE_SMOOTH);
            
            BufferedImage tag = new BufferedImage(afterwidth, afterheight, BufferedImage.TYPE_INT_RGB);  //缩放图像  
            Graphics g = tag.getGraphics();  
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            ImageIO.write(tag,"JPEG", bos);// 输出到bos  
            
            String scaledimage = DatatypeConverter.printBase64Binary(bos.toByteArray());
            bos.close();
            
            StateCache.getInstance().setUserState(targetuser, "UserPhotoSnippet", scaledimage);
        } catch (Exception e) {  
            Log.log(f_servername, Type.INFO, e); 
        }  
    }
}

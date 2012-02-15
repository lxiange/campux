package com.bizdata.campux.server.userinfo;

import java.util.LinkedList;
import java.util.List;

import com.bizdata.campux.sdk.util.DatatypeConverter;

import com.bizdata.campux.sdk.User;
import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.SAXHandlerBase;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.exception.ParseEndException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handle the received commands in xml format through SAX
 * @author yuy
 */
public class SAXHandler extends SAXHandlerBase{
    // defining commands
    enum Command{
        LIST_PUBLISHERS(110,"lp"), 
        REGISTER_PUBLISHER_TABLE(120,"rp"), 
        INITIALIZE_PUBLISHER_ACCOUNT(130,"ui"),
        CHECK_MsgBox_UPDATE(210,"ci"),
        GET_MsgBox_MsgIndex(220,"gs"),
        GET_MsgBlock_Message(230,"gi"),
        PUBLISH_Message(310,"si"),
        DELETE_Message(320,"di");
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
    Message message=new Message();
    // store the events
    LinkedList<Event> events;
    Event event;   
    // store the publihser
    Publisher pub;
    
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
            if( m_state == Command.PUBLISH_Message.state() ){
                m_users = new LinkedList<String>();
            }
        }else{
        	m_tagname = qName;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if( m_state<=0)
            return;
        m_content = new String(ch, start, length).trim();
        if( "t".equalsIgnoreCase(m_tagname)){
        	message.m_title=m_content;
        }else if("d".equalsIgnoreCase(m_tagname)){
        	message.m_date=m_content;
        }else if("l".equalsIgnoreCase(m_tagname)){
        	message.m_link=m_content;
        }else if("co".equalsIgnoreCase(m_tagname)){
        	if( m_attr.getValue("b64")!=null ){
                byte[] bytes = DatatypeConverter.parseBase64Binary(m_content);
                m_content = new String(bytes, Config.getCharset());
            }
        	message.m_content=m_content;
        }else if("p".equalsIgnoreCase(m_tagname)){
        	message.m_publisher=m_content;
        }else if("c".equalsIgnoreCase(m_tagname)){
        	message.m_pubcategory=m_content;
        }else if("es".equalsIgnoreCase(m_tagname)){
        	message.e_size=Integer.parseInt(m_content);
        	if(message.e_size!=0){
        		events=new LinkedList<Event>();        		
        	}
        		events=new LinkedList<Event>();
        }else if("et".equalsIgnoreCase(m_tagname)){
        	event=new Event();
        	if( m_attr.getValue("b64")!=null ){
                byte[] bytes = DatatypeConverter.parseBase64Binary(m_content);
                m_content = new String(bytes, Config.getCharset());
            }
        	event.event_title=m_content;
        }else if("ed".equalsIgnoreCase(m_tagname)){
        	event.event_date=m_content;
        }else if("ea".equalsIgnoreCase(m_tagname)){
        	if( m_attr.getValue("b64")!=null ){
                byte[] bytes = DatatypeConverter.parseBase64Binary(m_content);
                m_content = new String(bytes, Config.getCharset());
            }
        	event.event_place=m_content;
        	events.add(event);
        }else if("u".equalsIgnoreCase(m_tagname)){
        	m_users.add(m_content);
        }else if( "g".equalsIgnoreCase(m_tagname) ){
            m_groups.add(m_content);
        }else if("ip".equalsIgnoreCase(m_tagname)){
        	pub=new Publisher();
        	if( m_attr.getValue("b64")!=null ){
                byte[] bytes = DatatypeConverter.parseBase64Binary(m_content);
                m_content = new String(bytes, Config.getCharset());
            }
        	pub.p_iconname=m_content;
        }else if( "dp".equalsIgnoreCase(m_tagname) ){
            pub.p_displayname=m_content;
        }else if("cp".equalsIgnoreCase(m_tagname)){
        	pub.p_infotype.add(m_content);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if( m_state==0 ){
            //not needed
        }else{
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_state = 0;
                    fire(cmd);
                    throw new ParseEndException();
                }
            }
        }
    }
    
    
    /**
     * function that maps the commands into corresponding functions
     * @param cmd 
     */
    protected void fire(Command cmd) {
        try{
            System.out.println("fire:"+cmd);
            switch(cmd){
                case LIST_PUBLISHERS: 
                    func_LIST_PUBLISHERS();
                    break;
                case REGISTER_PUBLISHER_TABLE:
                    func_REGISTER_PUBLISHER_TABLE(m_attr, m_content);
                    break;
                case INITIALIZE_PUBLISHER_ACCOUNT: 
                    func_INITIALIZE_PUBLISHER_ACCOUNT(m_attr, m_content);
                    break;
                case CHECK_MsgBox_UPDATE:
                    func_CHECK_MsgBox_UPDATE(m_attr);
                    break;
                case GET_MsgBox_MsgIndex:
                    func_GET_MsgBox_MsgIndex(m_attr,m_content);
                    break;
                case GET_MsgBlock_Message:
                   func_GET_MsgBlock_Message(m_attr, m_content);
                    break;
                case PUBLISH_Message:
                    func_Publish_Message(m_attr, m_users,m_groups);
                    break;
                case DELETE_Message:
                    func_Delete_Message(m_attr, m_content);
                    break;
            }
        }catch(Exception exc){
            responseError(0, exc.getMessage());
        }
    }
    /**
     * function for list PUBLISHER items
     */
    protected void func_LIST_PUBLISHERS(){
    	StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok>");
        
        LinkedList<Publisher> pubs=PublisherCache.getInstance().displayAllPublisher();
        for(Publisher pub:pubs){
        	strbuilder.append("<a n=\""+pub.m_user+"\">");
        	strbuilder.append("<ip b64=\"true\">");
        	String b64 = DatatypeConverter.printBase64Binary(pub.p_iconname.getBytes(Config.getCharset()));
    		strbuilder.append(b64);
    		strbuilder.append("</ip>");
    		strbuilder.append("<dp>"+pub.p_displayname+"</dp>");
    		for(String type:pub.p_infotype){
    			strbuilder.append("<cp>"+type+"</cp>");
    		}
        	strbuilder.append("</a>");
        }
        strbuilder.append("</ok>");
        response(strbuilder.toString());
    }
    /**
     * function for register new publisher
     * @param attr
     * @param content 
     */
    protected void func_REGISTER_PUBLISHER_TABLE(Attributes attr,String content)throws Exception{
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
        Log.log("UserInfoServer", Type.INFO, "read for: " + usd + ":"+ user);
        
        if(PublisherCache.getInstance().registerNewPublisher(pub))
            response("<ok></ok>");
        else
        	responseError(302,"publusher has existed");
    	
    }
    /**
     * function for initialize new user count
     * @param attr
     * @param content 
     */
    protected void func_INITIALIZE_PUBLISHER_ACCOUNT(Attributes attr,String content){
    	String usd = attr.getValue("s");        
        User userauth = new User();
        String user = userauth.lookupUsername(usd);                         
        Log.log("UserInfoServer", Type.INFO, "read for: " + usd + ":"+ user);
        UserMessage.getInstance().initialNewUser(user);
        response("<ok></ok>");
    }
    /**
     * function for check message box update
     * @param attr
     */
    protected void func_CHECK_MsgBox_UPDATE(Attributes attr){
    	StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok>");
        
        String user="guest";
        String usd = attr.getValue("s");        
        if(usd!=null){
        	User userauth = new User();
            user = userauth.lookupUsername(usd);         
        }        
        Log.log("UserInfoServer", Type.INFO, "read for: " + usd + ":"+ user);        
        MsgIndex index=UserMessage.getInstance().getNewIndex(user, -1)[0];
        int newID=index.i_id;
        strbuilder.append(newID);
        strbuilder.append("</ok>");
        response(strbuilder.toString());
    }
    /**
     * function for get new items from message box
     * @param attr
     * @param content     
     */
    protected void func_GET_MsgBox_MsgIndex(Attributes attr,String content){
    	StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok>");
        
        String usd = attr.getValue("s");        
        User userauth = new User();
        String user = userauth.lookupUsername(usd);                         
        Log.log("UserInfoServer", Type.INFO, "read for: " + usd + ":"+ user);
        
        int initialindexid = Integer.parseInt(content);
        MsgIndex[] indices=UserMessage.getInstance().getNewIndex(user, initialindexid);
        
        if(indices==null){
        	responseError(301,"No message");
        	return;
        }else{
        	for(MsgIndex index:indices){
        		strbuilder.append("<i xp=\"");
        		strbuilder.append(index.i_publisher);
        		strbuilder.append("\" b64=\"true\" xc=\"");
        		strbuilder.append(index.i_pubcategory);
        		strbuilder.append("\" xd=\"");
        		strbuilder.append(index.i_id);
        		strbuilder.append("\">");
        		String b64 = DatatypeConverter.printBase64Binary(index.i_preview.getBytes(Config.getCharset()));
        		strbuilder.append(b64);
        		strbuilder.append("</i>");        		
        	}
        }
        strbuilder.append("</ok>");
        response(strbuilder.toString());
    }
    /**
     * function for get a item from message block
     * @param attr
     * @param content      
     */
    protected void func_GET_MsgBlock_Message(Attributes attr,String content){
    	StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok>");
        
        String usd = attr.getValue("s");        
        User userauth = new User();
        String user = userauth.lookupUsername(usd);                         
        Log.log("UserInfoServer", Type.INFO, "read for: " + usd + ":"+ user);
        
        int indexid = Integer.parseInt(content);
        MsgIndex index=UserMessage.getInstance().getIndex(user, indexid);
        
        if(index==null){
        	responseError(302,"No such message");
        	return;
        }else{
        	Message msg=UserMessage.getInstance().readMessage(index);
        	strbuilder.append("<t b64=\"true\">");
    		strbuilder.append(msg.m_title);
    		strbuilder.append("</t> <d>");
    		strbuilder.append(msg.m_date);
    		strbuilder.append("</d> <l>");
    		strbuilder.append(msg.m_link);
    		strbuilder.append("</l> <co b64=\"true\">");
    		String b64 = DatatypeConverter.printBase64Binary(msg.m_content.getBytes(Config.getCharset()));
    		strbuilder.append(b64);
    		strbuilder.append("</co>");
    		for(Event e: msg.events){
    			strbuilder.append("<e>");
    			strbuilder.append("<et b64=\"true\">");
    			b64 = DatatypeConverter.printBase64Binary(e.event_title.getBytes(Config.getCharset()));
    			strbuilder.append(b64);
    			strbuilder.append("</et> <ed>");
    			strbuilder.append(e.event_date);
    			strbuilder.append("</ed> <ea b64=\"true\">");
    			b64 = DatatypeConverter.printBase64Binary(e.event_place.getBytes(Config.getCharset()));
    			strbuilder.append(b64);
    			strbuilder.append("</ea></e>");    			
    		}
        }
        strbuilder.append("</ok>");
        response(strbuilder.toString());
    }
    /**
     * function for publish a message to a message block
     * @param attr
     * @param users    
     * @param groups 
     */
    protected void func_Publish_Message(Attributes attr, List<String> users, List<String> groups)throws Exception{
    	StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok>");
        
        String usd = attr.getValue("s");        
        User userauth = new User();
        String user = userauth.lookupUsername(usd);                         
        Log.log("UserInfoServer", Type.INFO, "read for: " + usd + ":"+ user);
        
        
        LinkedList<String> targetusers = new LinkedList<String>(users);
        for(String group:groups){
            String[] groupusers = userauth.groupUsers(group);
            for(String groupuser : groupusers){
                targetusers.add(groupuser);
            }
        }
        
        UserMessage.getInstance().putMessage(message, targetusers);
        response("<ok></ok>");
    }
    /**
     * function for delete a message from a message block
     * @param attr
     * @param content    
     */
    protected void func_Delete_Message(Attributes attr,String content){
    	StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("<ok>");
        
        String usd = attr.getValue("s");        
        User userauth = new User();
        String user = userauth.lookupUsername(usd);                         
        Log.log("UserInfoServer", Type.INFO, "read for: " + usd + ":"+ user);
        
        int deleteindexid = Integer.parseInt(content);
        UserMessage.getInstance().deleteMessage(user, deleteindexid);
        response("<ok></ok>");
    }
     

}


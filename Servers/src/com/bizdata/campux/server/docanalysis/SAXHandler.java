package com.bizdata.campux.server.docanalysis;

import com.bizdata.campux.sdk.User;
import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.Log.Type;
import com.bizdata.campux.server.SAXHandlerBase;
import com.bizdata.campux.server.exception.ParseEndException;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handle the received commands in xml format through SAX
 * @author yuy
 */
public class SAXHandler extends SAXHandlerBase{
    // defining commands
    enum Command{
        REGISTER_TASK(110,"r"), 
        TRAIN(120,"b"),
        PREDICT(130,"p");
        int m_state;
        String m_string;
        Command(int state, String string){
            m_state = state;
            m_string = string;
        }
        public String string() {return m_string;}
        public int state(){return m_state;}
    }
    final String f_servername = "DocAnalysisServer";
    
    // state of the current system
    int m_state = 0;
    // store temporary content for parsing XML
    String m_content;
    // store tagname
    String m_tagname;
    // store users publish to
    boolean m_b64 = false;
    // store task name
    String m_task;
    // store the message
    String m_usersession;
    // is append
    boolean m_append = false;
    
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
                    m_usersession = attributes.getValue("s");
                    m_task = attributes.getValue("n");
                    String tmp = attributes.getValue("b64");
                    if( tmp!=null )
                        m_b64=true;
                    tmp = attributes.getValue("append");
                    if( tmp!=null )
                        m_append = true;
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
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_state = 0;
                    fire(cmd);
                    throw new ParseEndException();
                }
            }
        }
        // clean up
        m_tagname = null;
        m_content = null;
        m_usersession = null;
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
    }    
    
    /**
     * function that maps the commands into corresponding functions
     * @param cmd 
     */
    protected void fire(Command cmd) {
        try{
            System.out.println("fire:"+cmd);
            switch(cmd){
                case REGISTER_TASK: 
                    func_REGISTER_TASK();
                    break;
                case TRAIN:
                    func_TRAIN();
                    break;
                case PREDICT:
                    func_PREDICT();
                    break;
            }
        }catch(Exception exc){
            responseError(0, exc.getMessage());
        }
    }
    /**
     * function for registering a task and appending data sets.
     */
    protected void func_REGISTER_TASK(){
        //User userauth = new User();
        String user = null; //userauth.lookupUsername(m_usersession); // go get user id;
        
        Log.log(f_servername, Type.INFO, "register task: " + m_task + " for "+ user);
        /*if( user==null ){
            responseError(0, "user not valid");
            return;
        }*/
        
        // store data
        try{
            
        }catch(Exception exc){
            Log.log("DocAnalysis", Type.ERROR, exc);
            responseError(0, exc.getMessage());
            return;
        }
        response("<ok></ok>");
    }
    /**
     * function for building models
     */
    protected void func_TRAIN(){
        
    }
    /**
     * function for prediction
     * @param attr
     * @param content 
     */
    protected void func_PREDICT(){
        //User userauth = new User();
        String user = null;//userauth.lookupUsername(m_usersession); // go get user id;
        
        Log.log(f_servername, Type.INFO, "read for: " + m_usersession + ":"+ user);
        /*if( user==null ){
            responseError(0, "user not valid");
            return;
        }*/
        
        String data = m_content;
        String task = m_task;
        
        try{
            Models models = Models.getInstance();
            int v = models.predictInstance(task, data);
            //locator.addData(m_item);
        }catch(Exception exc){
            Log.log(f_servername, Type.ERROR, exc);
            responseError(0, "error analyzing document: " + exc.getMessage());
        }
        response("<ok></ok>");
    }
}
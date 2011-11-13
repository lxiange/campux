package com.bizdata.campux.server.userprofile;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.SAXHandlerBase;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
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
        LIST_APP_VARIABLES(210,"ucl"),
        READ_APP_VARIABLE(220,"ucr"),
        WRITE_APP_VAIRABLE(230,"ucw");
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
    int m_state = -1;
    
    // store temporary attributes for parsing XML
    Attributes m_attr;
    // store temporary content for parsing XML
    String m_content;
    
    public SAXHandler(OutputStream outputstream){
         super(outputstream);
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if( m_state==-1){
            if( "x".equalsIgnoreCase(qName) )
                m_state=0;
        }else if(m_state==0){
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_state = cmd.state();
                    m_attr = attributes;
                    m_content = null;
                    break;
                }
            }
        }else{
            // not needed here
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if( m_state<=0)
            return;
        m_content = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if( m_state==-1 )
            ;// not needed here
        else if( m_state==0 ){
            if( "x".equalsIgnoreCase(qName) )
                m_state=-1;
        }else{
            for(Command cmd : Command.values()){
                if( cmd.string().equalsIgnoreCase(qName) ){
                    m_state = 0;
                    fire(cmd);
                    break;
                }
            }
        }
    }
    /**
     * function that maps the commands into corresponding functions
     * @param cmd 
     */
    protected void fire(Command cmd){
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
            case LIST_APP_VARIABLES: break;
            case READ_APP_VARIABLE: break;
            case WRITE_APP_VAIRABLE: break;
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
        String user = "notexistuser"; // go get user id;
        
        String varname = content;
        String val = StateCache.getInstance().getUserState(user, varname);
        
        String val64 = DatatypeConverter.printBase64Binary(val.getBytes(Config.getCharset()));
        
        strbuilder.append(val64);
        strbuilder.append("</ok>");
        
        response(strbuilder.toString());
    }
    /**
     * function for write system variables, once at a time
     * @param attr
     * @param content 
     */
    protected void func_WRITE_SYS_VAIRABLE(Attributes attr, String content){
        String usd = attr.getValue("s");
        String user = null; // go get user id;
        
        String varname = attr.getValue("n");
        
        String b64 = attr.getValue("b64");
        
        String val = content;
        
        if( b64!=null ){
            byte[] bytes = DatatypeConverter.parseBase64Binary(val);
            val = new String(bytes, Config.getCharset());
        }
        
        if( !StateCache.getInstance().setUserState(user, varname, val) ){
            responseError(151,"No such system variable");
        }else{        
            response("<ok></ok>");
        }
    }
}

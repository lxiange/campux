package com.bizdata.campux.server.userinfo;

import com.bizdata.campux.server.SAXHandlerBase;
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
    int m_state = 0;
    
    // store temporary attributes for parsing XML
    Attributes m_attr;
    // store temporary content for parsing XML
    String m_content;
    
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
    protected void fire(Command cmd){
        System.out.println("fire:"+cmd);
        switch(cmd){
            case LIST_SYS_VARIABLES: 
                func_LIST_SYS_VARIABLES();
                break;
            case READ_SYS_VARIABLE:
                func_READ_SYS_VARIABLE(m_attr, m_content);
                break;
        }
    }
    /**
     * function for list system variables
     */
    protected void func_LIST_SYS_VARIABLES(){
    
    }
    /**
     * function for read system variables, once at a time
     * @param attr
     * @param content 
     */
    protected void func_READ_SYS_VARIABLE(Attributes attr, String content){
        
    }
}


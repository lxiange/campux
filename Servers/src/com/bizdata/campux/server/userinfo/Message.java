package com.bizdata.campux.server.userinfo;
import java.util.*;

/**
*
* @author gl
* @date 2011-12-21 11:30:25
*/
class Message {
    public String m_title;
    public String m_date;
    public String m_link;
    public String m_publisher;
    public String m_pubcategory;    
    public String m_content;
    public int e_size=0;
   
    public LinkedList<Event> events=new LinkedList<Event>();
    
    public int m_id=-1;
    
    public Message(){}
    public String toString(){
    	String str="title: "+m_title+"    "+"date: "+m_date+"    "+"link: "+m_link+"\r\n";
    	str+="publisher: "+m_publisher+"    "+"pub_category: "+m_pubcategory+"\r\n";
    	str+="content: "+m_content+"\r\n";
    	str+="event_size: "+e_size+"\r\n";
    	for(Event e:events)
    		str+="event_title: "+e.event_title+"    event_date: "+e.event_date+"    event_place: "+e.event_place+"\r\n";
    	return str;
    }
    public Message(String t,String d,String l,String pu,String p,String c,LinkedList<Event> e){
    	m_title=t;
    	m_date=d;
    	m_link=l;
    	m_publisher=pu;
    	m_pubcategory=p;
    	m_content=c;
    	events=e;
    	if(events!=null)
    	    e_size=events.size();
    }
}

class Event{
	public String event_title;
	public String event_date;
	public String event_place;
	public Event(){}
	public Event(String t,String d,String p){
		event_title=t;
		event_date=d;
		event_place=p;	
	}
}
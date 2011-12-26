package com.bizdata.campux.server.userinfo;


/**
*
* @author gl
* @date 2011-12-21 11:39:00
*/


public class MsgIndex implements java.io.Serializable{
    public String i_block;
    public long i_start;
    public int i_id;
    public String i_title;
    public String i_date;
    public String i_publisher;
	public String i_pubcategory;    
    public String i_preview;
    
   
    public String preview;
    public MsgIndex clone(){
        MsgIndex newindex = new MsgIndex();
        newindex.i_block=i_block;
        newindex.i_start = i_start;
        newindex.i_id= i_id;
        newindex.i_title=i_title;
        newindex.i_date=i_date;
        newindex.i_publisher=i_publisher;
        newindex.i_pubcategory=i_pubcategory;
        newindex.i_preview=i_preview;
        return newindex;
    }
    public String toString(){
    	String str="block: "+i_block+"    ";
    	str+="start: "+i_start+"    "+"id: "+i_id+"\r\n";
    	str+="publisher: "+i_publisher+"    "+"publish_category: "+i_pubcategory+"\r\n";
    	str+="preview: "+i_preview+"    ";
    	return str;
    }
    public MsgIndex(){}
    public MsgIndex(String b,long l,int i,String title,String date,String pub,String p,String t,String pr){
    	i_block=b;
    	i_start=l;
    	i_id=i;
    	i_title=title;
    	i_date=date;
    	i_publisher=pub;
    	i_pubcategory=p;
    	i_preview=pr;
    }
}
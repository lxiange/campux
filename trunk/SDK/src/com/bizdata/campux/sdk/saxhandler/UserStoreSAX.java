/*
 * Copyright (C) 2011 Nanjing Bizdata-infotech co., ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bizdata.campux.sdk.saxhandler;

import java.util.LinkedList;
import org.xml.sax.Attributes;
/**
 * handles the user store
 * @author gl
 */
public class UserStoreSAX extends SAXHandlerBase {

	protected LinkedList<String> m_vars = new LinkedList<String>();
    protected String m_responseStr = null;
    
    public String getResponseString(){
        return m_responseStr;
    }
    
    public String[] getList(){
        String[] vs = new String[m_vars.size()];
        for(int i=0; i<vs.length; i++){
            vs[i] = m_vars.get(i);
        }
        return vs;
    }
    
    @Override
    protected void contentReceived(String content, String tagname, Attributes tagattr){
        System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }else if( "g".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }else if( "u".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }
    }

    public String prepareAppList(String sessionID){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<mul><si>");
        str.append(sessionID);
        str.append("</si></mul>\r\n");
        return str.toString();
    }
    
    public String prepareAppAdd(String sessionID, String app)
    {
    	 StringBuilder str = new StringBuilder();
         //str.append( Config.getXMLfirstline() );
         str.append("<mua><si>");
         str.append(sessionID);
         str.append("</si><u>");
         str.append(app);
         str.append("</u></mua>\r\n");
    //     System.out.println(str);
         return str.toString();
    }

    public String prepareAppDelete(String sessionID, String app){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<mud><si>");
        str.append(sessionID);
        str.append("</si><u>");
        str.append(app);
        str.append("</u></mud>\r\n");
   //     System.out.println(str);
        return str.toString();
    }

    public String prepareAppGetSpace(String sessionID,String app){
    	StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<mur><si>");
        str.append(sessionID);
        str.append("</si><u>");
        str.append(app);
        str.append("</u></mur>\r\n");
        System.out.println(str);
        return str.toString();
    }
    
    public String prepareAppSetSpace(String sessionID,String app,String len){
    	StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<mus><si>");
        str.append(sessionID);
        str.append("</si><u>");
        str.append(app);
        str.append("</u><q>");
        str.append(len);
        str.append("</q></mus>\r\n");
 //       System.out.println(str);
        return str.toString();
    }
    
    
    public String prepareDirList(String sessionID,String path){
    	StringBuilder str = new StringBuilder();
        str.append("<l><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(path);
        str.append("</p></l>\r\n");
     //   System.out.println(str);
        return str.toString();
    }

    public String prepareDirAdd(String sessionID,String path){
    	StringBuilder str = new StringBuilder();
        str.append("<md><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(path);
        str.append("</p></md>\r\n");
  //      System.out.println(str);
        return str.toString();
    }
    
    public String prepareDirDelete(String sessionID,String path){
    	StringBuilder str = new StringBuilder();
        str.append("<r><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(path);
        str.append("</p></r>\r\n");
        System.out.println(str);
        return str.toString();
    }
    
    public String prepareFileExist(String sessionID,String file){
    	StringBuilder str = new StringBuilder();
        str.append("<e><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(file);
        str.append("</p></e>\r\n");
 //       System.out.println(str);
        return str.toString();
    }

    public String prepareFileGetProperty(String sessionID,String file){
    	StringBuilder str = new StringBuilder();
        str.append("<fa><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(file);
        str.append("</p></fa>\r\n");
  //      System.out.println(str);
        return str.toString();
    }
    
    public String prepareFileRead(String sessionID, String file, String begin,String end){
    	StringBuilder str = new StringBuilder();
        str.append("<fr><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(file);
        str.append("</p><b>");
        str.append(begin);
        str.append("</b><e>");
        str.append(end);
        str.append("</e></fr>\r\n");
 //       System.out.println(str);
        return str.toString();
    }

    public String prepareFileWrite(String sessionID,String file,String begin,String content){
    	StringBuilder str = new StringBuilder();
        str.append("<fw><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(file);
        str.append("</p><b>");
        str.append(begin);
        str.append("</b><d><![CDATA[");
        str.append(content);
        str.append("]]></d></fw>\r\n");
 //       System.out.println(str);
        return str.toString();
    }
    
    public String prepareFileDelete(String sessionID,String file){
    	StringBuilder str = new StringBuilder();
        str.append("<df><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(file);       
        str.append("</p></df>\r\n");
//        System.out.println(str);
        return str.toString();
    }
    
    
}

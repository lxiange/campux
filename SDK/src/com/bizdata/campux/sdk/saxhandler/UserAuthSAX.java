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

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.util.LinkedList;
import org.xml.sax.Attributes;

/**
 * handles the user authentication
 * @author yuy
 */
public class UserAuthSAX extends SAXHandlerBase {    
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
    protected void contentReceived(String content, String tagname){
        System.out.println("contentrecevied:"+tagname);
        if("ok".equalsIgnoreCase(tagname)){
            m_responseStr = content;
        }else if( "g".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }else if( "u".equalsIgnoreCase(tagname) ){
            m_vars.add(content);
        }
    }
    
    public String prepareLogin(String user, String psw){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<v><u>");
        str.append(user);
        str.append("</u><p>");
        str.append( psw );
        str.append( "</p></v>\r\n" );
        return str.toString();
    }
    
    public String prepareLogout(String sessionID){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<o><s>");
        str.append(sessionID);
        str.append("</s></o>\r\n" );
        return str.toString();
    }
    
    public String prepareRegistration(String user, String psw, String name){
        StringBuilder str = new StringBuilder();
        
        //str.append( Config.getXMLfirstline() );
        str.append("<a><u>");
        str.append(user);
        str.append("</u><p>");
        str.append(psw);
        str.append("</p>");
        if( name==null || name.isEmpty()){
            str.append("<n></n>");
        }else{
            String b64 = DatatypeConverter.printBase64Binary(name);
            str.append("<n b64=\"true\">");
            str.append(b64);
            str.append("</n>");
        }
        str.append("</a>\r\n");
        return str.toString();
    }
    
    public String prepareLookup(String sessionID){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<c><s>");
        str.append(sessionID);
        str.append("</s></c>\r\n");
        return str.toString();
    }
    /**
     * 用户密码修改
     * @param sessionID 用户登录sessionID
     * @param psw 新密码
     * @return 
     */
    public String preparePasswordChange(String sessionID, String psw){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<m><si>");
        str.append(sessionID);
        str.append("</si><p>");
        str.append(psw);
        str.append("</p></m>\r\n");
        return str.toString();
    }
    //用户删除
    public String prepareUserDelete(String sessionID, String usr){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<d><si>");
        str.append(sessionID);
        str.append("</si><u>");
        str.append(usr);
        str.append("</u></d>\r\n");
        return str.toString();
    }
    //用户组添加
    public String prepareGroupAdd(String sessionID, String group){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<ga><si>");
        str.append(sessionID);
        str.append("</si><g>");
        str.append(group);
        str.append("</g></ga>\r\n");
        return str.toString();
    }
    //用户组删除
    public String prepareGroupDelete(String sessionID, String group){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<gd><si>");
        str.append(sessionID);
        str.append("</si><g>");
        str.append(group);
        str.append("</g></gd>\r\n");
        return str.toString();
    }
    //用户组枚举
    public String prepareGroupList(String sessionID){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<gl><si>");
        str.append(sessionID);
        str.append("</si></gl>\r\n");
        return str.toString();
    }
    //用户的组属性枚举
    public String prepareUserBelongingGroups(String sessionID, String usr){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<ug><si>");
        str.append(sessionID);
        str.append("</si><u>");
        str.append(usr);
        str.append("</u></ug>\r\n");
        return str.toString();
    }
    //用户组内的用户名枚举
    public String prepareGroupUserList(String sessionID, String group){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<gul><si>");
        str.append(sessionID);
        str.append("</si><g>");
        str.append(group);
        str.append("</g></gul>\r\n");
        return str.toString();
    }
    //用户关联到用户组
    public String prepareUserAssociateGroup(String sessionID, String usr, String group){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<gua><si>");
        str.append(sessionID);
        str.append("</si><u>");
        str.append(usr);
        str.append("</u><g>");
        str.append(group);
        str.append("</g></gua>\r\n");
        return str.toString();
    }
    //用户取消到用户组的关联
    public String prepareUserDissociateGroup(String sessionID, String usr, String group){
        StringBuilder str = new StringBuilder();
        //str.append( Config.getXMLfirstline() );
        str.append("<gur><si>");
        str.append(sessionID);
        str.append("</si><u>");
        str.append(usr);
        str.append("</u><g>");
        str.append(group);
        str.append("</g></gur>\r\n");
        return str.toString();
    }

}

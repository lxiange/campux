/*
 * Copyright (C) 2012 Nanjing Bizdata-infotech co., ltd.
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
package com.bizdata.campux.sdk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 * @author yuy
 * @date 2012-02-28 06:05:29
 */
public class Init {
    static public void main(String[] args) throws Exception{
        /*User admin = new User();
        admin.login("admin", "_bizdata");
        admin.groupAdd("system");
        admin.groupAssociateToUser("system", "system_service");
        String[] gs = admin.groupUsers("system");
        */
        User usr = new User();
        usr.login("system_service", "sysuser_bizdata");
        //String[] groups = usr.userGroups("system_service");
        Info info = new Info(usr);
        
        BufferedReader reader = new BufferedReader(new FileReader("newfile"));
        String line=null;
        
        int index=1;
        while( (line=reader.readLine()) !=null ){
            System.out.println(index++);
            
            String[] seps = line.split("\\t");
            if( seps.length!=4 ){
                System.out.println("error: " +seps.length+"::"+ line);
                return;
            }
            String userid = seps[0].trim();
            String name = seps[1].trim();
            String school = seps[2].trim();
            String watch = seps[3].trim();
            usr.setUserVariable(userid, "UserName", name);
            if( !school.isEmpty()) 
                usr.setUserVariable(userid, "UserSchool", school);
            
            boolean graduate = userid.charAt(0) > '9';
            String num = graduate ? userid.substring(2,4) : userid.substring(0,2);
            if( num.charAt(0) > '1' )
                num = "19" + num;
            else
                num = "20" + num;
            usr.setUserVariable(userid, "UserGrade", num);
            
            List<String> list = info.infoPublisherRead();
            for(String old:list)
                info.infoPublisherDel(old);
            info.infoPublisherAdd("PublisherNotice");
            info.infoPublisherAdd("PublisherRecruitment");
            info.infoPublisherAdd("PublisherTalk");
            info.infoPublisherAdd("NJUAnniversary");
            
            info.infoRoomAdd(userid, "NJUExpress");
            info.infoRoomAdd(userid, "M_Job");
            info.infoRoomAdd(userid, "JobAndWork");
            if( !watch.isEmpty())
                info.infoRoomAdd(userid, watch);
            if( graduate )
                info.infoRoomAdd(userid, "M_Academic");
            else
                info.infoRoomAdd(userid, "M_Graduate");
            
             usr.setUserVariable(userid, "Friends", userid);
        }
        reader.close();
    }
}

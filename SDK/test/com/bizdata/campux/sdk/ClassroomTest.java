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
import java.util.List;
import java.util.HashMap;
import org.junit.Ignore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yuy
 */
public class ClassroomTest {
    
    public ClassroomTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Ignore
    @Test
    public void publishClassroom() throws Exception {
        System.out.println("test");
        
        User usr = new User();
        //boolean succ = usr.register("system_service", "sysuser_bizdata", null, null, null, null, null, null);
        //assertTrue(succ);
        boolean suc = usr.login("001221154", "123456");
        assertTrue(suc);
        
        ClassRoom f = new ClassRoom(usr);
        
        BufferedReader reader = new BufferedReader(new FileReader("C:\\usr\\Campux\\classroom\\roomlist'.txt"));
        String room = null;
        while( (room=reader.readLine())!=null ){
            if( room.isEmpty() )
                continue;
            suc = f.publishClassRoomComment(room, true);
            assertTrue(suc);
            System.out.println(room);
        }
        reader.close();
    }
    
    @Test
    public void testClassroom() throws Exception {
        System.out.println("test");
        
        User usr = new User();
        //boolean succ = usr.register("system_service", "sysuser_bizdata", null, null, null, null, null, null);
        //assertTrue(succ);
        boolean suc = usr.login("001221154", "123456");
        assertTrue(suc);
        
        ClassRoom f = new ClassRoom(usr);
        
        HashMap<String, List<String>> roomlist = f.listClassRooms();
        for(String key : roomlist.keySet()){
            System.out.println(key+":");
            List<String> rooms = roomlist.get(key);
            if( rooms!=null ){
                for(String room : rooms)
                    System.out.println("    "+room + " " + f.readClassRoomComment(key, room));
            }
        }
        
        /*suc = f.publishClassRoomComment("仙林", "I120", true);
        assertTrue(suc);
        
        suc = f.publishClassRoomComment("仙林", "I120", true);
        assertTrue(suc);
        
        suc = f.publishClassRoomComment("仙林", "I220", false);
        assertTrue(suc);
        
        int v = f.readClassRoomComment("仙Ⅰ_102");
        System.out.println(v);*/
        
        roomlist = f.listClassRooms();
        for(String key : roomlist.keySet()){
            System.out.println(key+":");
            List<String> rooms = roomlist.get(key);
            if( rooms!=null ){
                for(String room : rooms){
                    int v = f.readClassRoomComment(key, room);
                    System.out.println("    "+room+":"+v);
                }
            }
        }
        usr.logout();
    }
    
}

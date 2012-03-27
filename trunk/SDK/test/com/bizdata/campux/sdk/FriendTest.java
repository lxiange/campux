package com.bizdata.campux.sdk;

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

import org.junit.Ignore;
import java.util.List;
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
public class FriendTest {
    
    public FriendTest() {
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
    public void testFriendAddListDel() throws Exception {
        System.out.println("test");
        
        User usr = new User();
        //boolean succ = usr.register("system_service", "sysuser_bizdata", null, null, null, null, null, null);
        //assertTrue(succ);
        boolean suc = usr.login("test", "test");
        assertTrue(suc);
        
        Friend f = new Friend(usr);
        
        //suc = f.friendDel("test");
        //assertTrue(suc);
        
        List<String> flist = f.friendRead("test");
        for(String fname : flist){
            System.out.println(fname);
        }
        
        //suc = f.friendAdd("test");
        //assertTrue(suc);
    }
    //@Ignore
    @Test
    public void testFriendInfoPublish() throws Exception {
        System.out.println("test");
        
        User usr = new User();
        //boolean succ = usr.register("system_service", "sysuser_bizdata", null, null, null, null, null, null);
        //assertTrue(succ);
        boolean suc = usr.login("test", "test");
        assertTrue(suc);
        
        /*try{
            User user = new User();
            user.login("system_service", "sysuser_bizdata");
            Friend fobj = new Friend(user);
            fobj.__friendStatusPublish("test", 0+"test status");
        }catch(Exception exc){
            exc.printStackTrace();
        }*/
        
        usr.setUserVariable("UserStatus", "test status 2");
        usr.setUserVariable("UserLocation", "test location 2");
        
        Friend f = new Friend(usr);
        
        FriendMessage[] fms = f.friendStatusUpdate();
        assertNotNull(fms);
        
        for(FriendMessage fm : fms){
            System.out.println(fm.publisher + " " + fm.type + 
                    " " + fm.time + " " + fm.message);
        }
    }
}

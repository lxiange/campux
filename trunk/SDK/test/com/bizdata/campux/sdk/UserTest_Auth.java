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
package com.bizdata.campux.sdk;

import java.io.FileInputStream;
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
public class UserTest_Auth {
    static User instance = null;
    public UserTest_Auth() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Config.init(new FileInputStream("sdk.config"));
        instance = new User();
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
    
    /**
     * Test of register method, of class User.
     */
    //@Ignore
    @Test
    public void testRegister() throws Exception{
        System.out.println("register");
        
        boolean result = instance.register("001221157", "123456", "俞扬", "计算机", null, null, "18", "M");
        assertTrue(result);
        
    }

    /**
     * Test of login method, of class User.
     */
    //@Ignore
    @Test
    public void testLogin() throws Exception {
        System.out.println("login");
        String name = "001221157";
        String psw = "123456";
        
        boolean result = instance.login(name, psw);
        
        assertTrue(result);
        
        System.out.println("SessionID:"+instance.getSessionID());
    }


    /**
     * Test of lookupUsername method, of class User.
     */
    //@Ignore
    @Test
    public void testLookupUsername() throws Exception {
        System.out.println("lookupUsername: " + instance.getSessionID());
        String userSessionID = instance.getSessionID();
        String result = instance.lookupUsername(userSessionID);
        System.out.println("Username:"+result);
        //assertEquals("admin", result);
    }    


    /**
     * Test of getUserVariables method, of class User.
     */
    @Ignore
    @Test
    public void testGetUserVariables() throws Exception {
        System.out.println("getUserVariables");
                // communication with the server
        String[] vars = null;
        try{
            vars = instance.getUserVariables();
        }catch(Exception exc){
            fail(exc.getMessage());
        }
        
        assertNotNull(vars);

        assertEquals(9,vars.length);

        assertEquals(vars[0],"UserLocation");
        assertEquals(vars[1],"UserStatus");
        assertEquals(vars[2],"UserName");
        assertEquals(vars[3],"UserDepartment");
        assertEquals(vars[4],"UserSchool");
        assertEquals(vars[5],"UserGrade");
        assertEquals(vars[6],"UserAge");
        assertEquals(vars[7],"UserGender");
        assertEquals(vars[8],"UserPhoto");
    }
    @Ignore
    @Test
    public void testSetAndGetUserVariable() throws Exception {
        System.out.println("set&GetUserVariable");
        
        String val = instance.getUserVariable("UserGrade");
        System.out.println("get UserGrade:"+val);
        instance.setUserVariable("UserGrade", "2000");
        val = instance.getUserVariable("UserGrade");
        assertEquals("2000", val);
        System.out.println("get UserGrade:"+val);
        
        instance.setUserVariable("UserGrade", "2004");
        val = instance.getUserVariable("UserGrade");
        assertEquals("2004", val);
        System.out.println("get UserGrade:"+val);
        
    }
    
        
    /**
     * Test of logout method, of class User.
     */
    @Ignore
    @Test
    public void testLogout() {
        System.out.println("logout");
        try{
            instance.logout();
        }catch(Exception exc){}
    }
}

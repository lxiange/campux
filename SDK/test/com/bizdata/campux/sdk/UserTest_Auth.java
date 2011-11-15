/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.sdk;

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
     * Test of login method, of class User.
     */
    @Test
    public void testLogin() throws Exception {
        System.out.println("login");
        String name = "001221154";
        String psw = "123456";
        
        boolean result = instance.login(name, psw);
        
        assertTrue(result);
        
        System.out.println("SessionID:"+instance.getSessionID());
    }


    /**
     * Test of register method, of class User.
     */
    @Ignore
    @Test
    public void testRegister() throws Exception{
        System.out.println("register");
        
        boolean result = instance.register("001221154", "123456", "俞扬", "计算机", null, null, "18", "M");
        assertTrue(result);
        
    }

    /**
     * Test of lookupUsername method, of class User.
     */
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
    //@Ignore
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
    @Test
    public void testLogout() {
        System.out.println("logout");
        instance.logout();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.userprofile;

import com.bizdata.campux.sdk.User;
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
public class ServerUserStatusTest {
    
    public ServerUserStatusTest() {
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

    /**
     * Test of startServer method, of class ServerUserStatus.
     */
    @Test
    public void testStartServer() {
        System.out.println("startServer");
        ServerUserStatus instance = new ServerUserStatus();
        instance.startServer();
        
        // communication with the server
        User user = new User();
        String[] vars = null;
        try{
            vars = user.getUserVariables();
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
}

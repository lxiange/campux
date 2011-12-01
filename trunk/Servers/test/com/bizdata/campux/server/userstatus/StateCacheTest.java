/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.userstatus;

import java.io.File;
import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.ConfigTest;
import com.bizdata.campux.server.userstatus.StateCache.State;
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
public class StateCacheTest {
    
    public StateCacheTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ConfigTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ConfigTest.tearDownClass();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class StateCache.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        assertNotNull(StateCache.getInstance());
    }

    /**
     * Test of getCacheSize method, of class StateCache.
     */
    @Test
    public void testGetCacheSize() {
        System.out.println("getCacheSize");
        assertEquals(100, StateCache.getInstance().getCacheSize());
    }

    /**
     * Test of getUserState method, of class StateCache.
     */
    @Test
    public void testGetAndSetUserState() {
        System.out.println("getUserState");
        
        File userfile = new File(Config.getValue("UserStateVariablePath")+"notexistuser.state");
        if( userfile.exists() )
            userfile.delete();
        
        String result = StateCache.getInstance().getUserState("notexistuser", "notexiststate");
        assertNull(result);
        
        assertTrue(userfile.exists());
        
        assertTrue(StateCache.getInstance().setUserState("notexistuser", "UserStudentID", "001221154"));
        assertEquals("001221154", StateCache.getInstance().getUserState("notexistuser", "UserStudentID"));
        
        assertTrue(StateCache.getInstance().setUserState("notexistuser", "UserStatus", "今天心情真好"));
        assertEquals("今天心情真好", StateCache.getInstance().getUserState("notexistuser", "UserStatus"));
        
        userfile.delete();
    }


    /**
     * Test of getVariables method, of class StateCache.
     */
    @Test
    public void testGetVariables() {
        System.out.println("getVariables");
        List<String> result = StateCache.getInstance().getVariables();        
        assertNotNull(result);

        assertEquals(result.size(), 9);

        assertEquals(result.get(0),"UserLocation");
        assertEquals(result.get(1),"UserStatus");
        assertEquals(result.get(2),"UserStudentID");
        assertEquals(result.get(3),"UserDepartment");
        assertEquals(result.get(4),"UserSchool");
        assertEquals(result.get(5),"UserGrade");
        assertEquals(result.get(6),"UserAge");
        assertEquals(result.get(7),"UserGender");
        assertEquals(result.get(8),"UserPhoto");
    }

}

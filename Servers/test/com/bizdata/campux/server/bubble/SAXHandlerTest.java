/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.bubble;

import com.bizdata.campux.server.Config;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.Attributes;

/**
 *
 * @author yuy
 */
public class SAXHandlerTest {
    
    public SAXHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Config.m_unittest = true;
        handler = new SAXHandler();
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

    static SAXHandler handler;
    /**
     * Test of startElement method, of class SAXHandler.
     */
    @Test
    public void testPublish() throws Exception {
        System.out.println("test publish");
        
        handler.m_content = "a test message 1";
        handler.m_usersession = "123";
        handler.func_PUBLISH();
        handler.m_content = "a test message 2";
        handler.m_usersession = "345";
        handler.func_PUBLISH();
    }

    /**
     * Test of characters method, of class SAXHandler.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("test read");
        
        handler.m_usersession = "123";
        handler.func_READ();
    }

}

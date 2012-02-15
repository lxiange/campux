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
public class BubbleTest {
    
    public BubbleTest() {
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
     * Test of clean method, of class Locator.
     */
    @Test
    public void testBubble() throws Exception {
        System.out.println("test");
        
        User usr = new User();
        //boolean succ = usr.register("system_service", "sysuser_bizdata", null, null, null, null, null, null);
        //assertTrue(succ);
        boolean suc = usr.login("test", "test");
        assertTrue(suc);
        
        suc = usr.setUserVariable("UserLocation", "testlocation");
        assertTrue(suc);
        
        Bubble bubble = new Bubble(usr);
        suc = bubble.publish("b1");
        assertTrue(suc);
        
        BubbleMessage[] msgs = bubble.bubbleRead(0);
        for(BubbleMessage msg : msgs){
            System.out.println(msg.publisher + " " + msg.time + " " + msg.message);
        }
        
        System.out.println("udpate");
        bubble.publish("b2");bubble.publish("b3");bubble.publish("b4");
        msgs = bubble.bubbleUpdate();
        for(BubbleMessage msg : msgs){
            System.out.println(msg.publisher + " " + msg.time + " " + msg.message);
        }
    }

}

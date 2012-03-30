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
public class LocatorTest {
    
    public LocatorTest() {
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
    @Ignore
    @Test
    public void testClean() {
        System.out.println("clean");
        Locator instance = new Locator();
        instance.clean();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addWifi method, of class Locator.
     */
    @Ignore
    @Test
    public void testAddWifi() {
        System.out.println("addWifi");
        String bssid = "";
        int strength = 0;
        boolean connected = false;
        Locator instance = new Locator();
        instance.addWifi(bssid, strength, connected);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setIP method, of class Locator.
     */
    @Ignore
    @Test
    public void testSetIP() {
        System.out.println("setIP");
        String ip = "";
        Locator instance = new Locator();
        instance.setIP(ip);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocation method, of class Locator.
     */
    @Test
    public void testGetLocation() throws Exception {
        System.out.println("getLocation");
        User user = new User();
        user.login("001221154", "123456");
        Locator instance = new Locator();
        String expResult = "";
        String result = instance.getLocation(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLocation method, of class Locator.
     */
    @Ignore
    @Test
    public void testSetLocation() throws Exception {
        System.out.println("setLocation");
        User user = null;
        String locationname = "";
        Locator instance = new Locator();
        instance.setLocation(user, locationname);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

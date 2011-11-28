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
package com.bizdata.campux.sdk.saxhandler;

import java.io.ByteArrayInputStream;
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
public class UserStatusSAXTest {
    
    public UserStatusSAXTest() {
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
     * Test of getVariables method, of class UserStatusSAX.
     */
    @Test
    public void testGetVariables() {
        System.out.println("getVariables");
        
        String fakeinput = "<ok><v>var1</v><v>var2</v></ok>";
                
        UserStatusSAX instance = new UserStatusSAX();
        try{
        instance.parseInput(new ByteArrayInputStream(fakeinput.getBytes()));
        }catch(Exception exc){
            fail(exc.getMessage());
        }
        
        assertFalse(instance.getIsError());
        
        String[] result = instance.getVariables();
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("var1", result[0]);
        assertEquals("var2", result[1]);
        
        String fakeinput2 ="<err c=\"123\">fake error test</err>";
        instance = new UserStatusSAX();
        try{
            instance.parseInput(new ByteArrayInputStream(fakeinput2.getBytes()));
        }catch(Exception exc){
            fail(exc.getMessage());
        }
        
        assertTrue(instance.getIsError());
        assertEquals(123, instance.getErrorCode());
        assertEquals("fake error test", instance.getErrorMsg());
    }
}

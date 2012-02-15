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

import java.io.FileInputStream;
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
public class UserStatusTest {
    
    public UserStatusTest() {
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
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void testUserstatus() throws Exception{
        Config.init(new FileInputStream("sdk.config"));
        User user = new User();
        boolean suc = user.login("test", "test");
        System.out.println("login:"+suc);
        
        String longstr = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        longstr += longstr;
        longstr += longstr;
        longstr += longstr;
        longstr += longstr;
        longstr += longstr;
        longstr += longstr;
        longstr += longstr;
        
        suc = user.setUserVariable("UserPhoto", longstr);
        System.out.println("write:"+suc);
        
        String verify = user.getUserVariable("UserPhoto");
        System.out.println(verify);
        
        System.out.println(longstr.equals(verify));
    }
}

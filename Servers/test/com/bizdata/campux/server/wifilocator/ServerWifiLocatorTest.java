/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.wifilocator;

import java.io.FileInputStream;
import com.bizdata.campux.server.userstatus.ServerUserStatus;
import com.bizdata.campux.sdk.Locator;
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
public class ServerWifiLocatorTest {
    
    public ServerWifiLocatorTest() {
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

    @Test
    public void testStartServer() throws Exception{
        com.bizdata.campux.sdk.Config.init(new FileInputStream("sdk.config"));
        System.out.println("startServer");
        ServerUserStatus uinstance = new ServerUserStatus();
        uinstance.startServer();
        ServerWifiLocator instance = new ServerWifiLocator();
        instance.startServer();
        
        // communication with the server
        User user = new User();
        
        Locator locator = new Locator();
        locator.addWifi("00:00:00:00:00:00", -70, true);
        try{
            locator.setLocation(user, "test");
        }catch(Exception exc){
            exc.printStackTrace();
        }
        
        locator = new Locator();
        locator.addWifi("90:84:0d:db:1d:e1", -96, false);
        locator.addWifi("00:24:01:c5:97:a0", -91, false);
        try{
            String str = locator.getLocation(user);
            System.out.println(str);
            assertEquals("体育场(仙林)", str);
        }catch(Exception exc){
            exc.printStackTrace();
        }
        locator.clean();
        locator.addWifi("00:00:00:00:00:00", -90, false);
        try{
            String str = locator.getLocation(user);
            assertEquals("test", str);
        }catch(Exception exc){
            exc.printStackTrace();
        }
        locator.clean();
        locator.addWifi("00:00:11:22:00:00", -90, false);
        try{
            String str = locator.getLocation(user);
            System.out.println("str:"+str);
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
}

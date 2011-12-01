/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.userstatus;

import java.util.Random;
import java.io.File;
import com.bizdata.campux.server.ConfigTest;
import java.util.LinkedList;
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
public class UserMessageTest {
    
    public UserMessageTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //ConfigTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //ConfigTest.tearDownClass();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    static Message oldmsg = null;
    /**
     * Test of putMessage method, of class UserMessage.
     */
    @Test
    public void testPutMessage() {
        System.out.println("putMessage");
        Message msg = new Message();
        msg.date =randomString();
        msg.message = randomString();
        msg.publisher=randomString();
        
        List<String> users = new LinkedList<String>();
        users.add("user1");
        users.add("user2");
        
        UserMessage instance = UserMessage.getInstance();
        instance.putMessage(msg, users);
        
        assertTrue(new File("users/user1.msgbox").exists());
        assertTrue(new File("users/user2.msgbox").exists());
        
        oldmsg=msg;
    }

    /**
     * Test of getNewMessage method, of class UserMessage.
     */
    @Test
    public void testGetNewMessage() {
        System.out.println("getNewMessage");
        
        UserMessage instance = UserMessage.getInstance();
        Message[] result = instance.getNewMessage("user1", -1);
        Message[] result2 = instance.getNewMessage("user2", -1);
        
        //assertEquals(3, result.length);
        //assertEquals(3, result2.length);
        
        assertEquals(oldmsg.message, result[0].message);
        assertEquals(oldmsg.date, result[0].date);
        assertEquals(oldmsg.publisher, result[0].publisher);
        
        assertEquals(oldmsg.message, result2[0].message);
        assertEquals(oldmsg.date, result2[0].date);
        assertEquals(oldmsg.publisher, result2[0].publisher);
        
        System.out.println("id: " + result[0].id + "  " + result2[0].id);
        
        assertTrue(instance.deleteMessage("user1", result[0].id));
        assertTrue(instance.deleteMessage("user2", result[0].id));
        
        assertEquals(4, new File("users/user1.msgbox").length());
        assertEquals(4, new File("users/user2.msgbox").length());
    }
    
    private String randomString(){
        Random rnd = new Random();
        int len = rnd.nextInt(100)+10;
        char[] chars = new char[len];
        for(int i=0; i<len; i++)
            chars[i] = (char)(rnd.nextInt(26) + 'a');
        return new String(chars,0,len);
    }
    
}

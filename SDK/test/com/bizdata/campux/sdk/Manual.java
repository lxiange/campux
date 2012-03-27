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

import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import org.junit.Ignore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
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
public class Manual {
    
    public Manual() {
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
    //@Ignore
    @Test
    public void registerSystemServices() throws Exception{
        File lflarge = new File("c:/usr/campux/lily-74.png");
        File lfsmall = new File("c:/usr/campux/lily-44.png");
        File aflarge = new File("c:/usr/campux/110logo-74.png");
        File afsmall = new File("c:/usr/campux/110logo-44.png");
        byte[] buffer = new byte[100000];
        byte[] b=null;
        
        FileInputStream in = new FileInputStream(lflarge);
        int size = in.read(buffer);
        b=new byte[size]; System.arraycopy(buffer, 0, b, 0, size);
        in.close();
        String llarge = DatatypeConverter.printBase64Binary(b);
        
        in = new FileInputStream(lfsmall);
        size = in.read(buffer);
        b=new byte[size]; System.arraycopy(buffer, 0, b, 0, size);
        in.close();
        String lsmall = DatatypeConverter.printBase64Binary(b);
        
        in = new FileInputStream(aflarge);
        size = in.read(buffer);
        b=new byte[size]; System.arraycopy(buffer, 0, b, 0, size);
        in.close();
        String alarge = DatatypeConverter.printBase64Binary(b);
        
        in = new FileInputStream(afsmall);
        size = in.read(buffer);
        b=new byte[size]; System.arraycopy(buffer, 0, b, 0, size);
        in.close();
        String asmall = DatatypeConverter.printBase64Binary(b);
        
        
        User usr = new User();
        boolean succ = usr.login("PublisherNotice", "bizdataService");
        assertTrue(succ);
        usr.setUserVariable("UserPhoto", llarge);
        usr.setUserVariable("UserPhoto", lsmall);
        
        succ = usr.login("PublisherRecruitment", "bizdataService");
        assertTrue(succ);
        usr.setUserVariable("UserPhoto", llarge);
        usr.setUserVariable("UserPhoto", lsmall);
        
        succ = usr.login("PublisherTalk", "bizdataService");
        assertTrue(succ);
        usr.setUserVariable("UserPhoto", llarge);
        usr.setUserVariable("UserPhoto", lsmall);
        
        succ = usr.login("NJUAnniversary", "bizdataService");
        assertTrue(succ);
        usr.setUserVariable("UserPhoto", alarge);
        usr.setUserVariable("UserPhoto", asmall);
        
        /*succ = usr.login("admin", "_bizdata");
        assertTrue(succ);
        succ = usr.groupAssociateToUser("system", "PublisherNotice");
        assertTrue(succ);
        succ = usr.groupAssociateToUser("system", "PublisherRecruitment");
        assertTrue(succ);
        succ = usr.groupAssociateToUser("system", "PublisherTalk");
        assertTrue(succ);
        succ = usr.groupAssociateToUser("system", "NJUAnniversary");
        assertTrue(succ);*/
    }
    @Ignore
    @Test
    public void testphoto() throws Exception{
        User usr=new User();
        usr.login("001221154", "123456");
        
        File f = new File("c:/usr/campux/head.jpg");
        FileInputStream in = new FileInputStream(f);
        byte[] a = new byte[(int)f.length()];
        in.read(a);
        in.close();
        
        String imgstr = DatatypeConverter.printBase64Binary(a);
        //String snippet = scale_photo(imgstr,a);
        
        boolean suc = usr.setUserVariable("UserPhoto", imgstr);
        System.out.println(suc);
        
        String snippet = usr.getUserVariable("UserPhotoSnippet");
        System.out.println(snippet.length());
        
        FileOutputStream out = new FileOutputStream("c:/usr/campux/heads.jpg");
        out.write(DatatypeConverter.parseBase64Binary(snippet));
        out.close();
    }
    
}

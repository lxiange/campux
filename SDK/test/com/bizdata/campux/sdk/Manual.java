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

import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.HashMap;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import org.junit.Ignore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
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
    //@Ignore
    @Test
    public void test() throws Exception{
        String str="5a6X56uL";
        String base64 = new String(DatatypeConverter.parseBase64Binary(str), Config.getCharset());
       System.out.println(base64);
    }
    @Ignore
    @Test
    public void verRegFile() throws Exception{
        String id = "mg1133023";
        String name = "鲁翔";
        String filehash = id.substring(0,4) + ".reginfo";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filehash)));
        String line = null;
        String infoline = null;
        while( (line=reader.readLine())!=null){
            if( id.equalsIgnoreCase(line.substring(0,id.length()))){
                infoline = line;
                break;
            }
        }
        if( infoline == null )
            return;
        String segs[] = infoline.split("\\t");
        String rid = segs[0];
        String rname = segs[1];
        String rdept = segs.length>=3 ? segs[2] : "";
        String rboard = segs.length>=4 ? segs[3] : "";
        System.out.println(rid + " " + rname + " " + rdept + " " + rboard);
        System.out.println(name.equalsIgnoreCase(rname));
        
        System.out.println(name + " " + rname);
	        {
	        	byte[] bs = name.getBytes(Charset.forName("UTF-8"));
	        	for(byte b:bs)
	        		System.out.print(b+"");
	        }
	        System.out.println("");
	        {
	        	byte[] bs = rname.getBytes();
                        String n=new String(bs,Charset.forName("UTF-8"));
                        bs = n.getBytes("UTF-8");
                        System.out.println(n);
	        	for(byte b:bs)
	        		System.out.print(b+"");
	        }
    }
    @Ignore
    @Test
    public void sepRegfile() throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\usr\\Campux\\campux\\SDK\\newfile"),Charset.forName("UTF-8")));
        HashMap<String, BufferedWriter> hash = new HashMap<String, BufferedWriter>();
        String line = null;
        while( (line=reader.readLine())!=null){
            if( line.length()<6 )
                continue;
            String prefix = line.substring(0,4);
            BufferedWriter writer = hash.get(prefix);
            if( writer==null ){
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(prefix+".reginfo"), Charset.forName("UTF-8")));
                hash.put(prefix, writer);
            }
            writer.write(line);
            writer.newLine();
        }
        reader.close();
        for(BufferedWriter w:hash.values()){
            w.close();
        }
    }
    //
    @Ignore
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

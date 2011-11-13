/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
public class ConfigTest {
    
    public ConfigTest() {
    }

    protected static byte[] m_storeConfigFile = null;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        String testconfig = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><config><StateCacheSize>100</StateCacheSize><StateVariable>UserLocation</StateVariable>"
                + "<StateVariable>UserStatus </StateVariable><UserStateVariablePath>./users/</UserStateVariablePath><StateVariable>UserStudentID</StateVariable>"
                + "<StateVariable>UserDepartment</StateVariable><StateVariable>UserSchool</StateVariable>"
                + "<StateVariable>UserGrade</StateVariable><StateVariable>UserAge</StateVariable>"
                + "<StateVariable>UserGender</StateVariable><StateVariable>UserPhoto</StateVariable></config>";
        File file = new File(".config");
        if( file.exists() ){
            try{
                BufferedInputStream read = new BufferedInputStream(new FileInputStream(file));
                long size = file.length();
                m_storeConfigFile = new byte[(int)size];
                read.read(m_storeConfigFile);
                read.close();
                
                BufferedWriter write = new BufferedWriter(new FileWriter(file));
                write.write(testconfig);
                write.close();
            }catch(Exception exc){
                fail(exc.getMessage());
            }
        }else{
            m_storeConfigFile = null;
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        File file = new File(".config");
        if( m_storeConfigFile == null){
            file.delete();
        }else{
            BufferedOutputStream write = new BufferedOutputStream(new FileOutputStream(file));
            write.write(m_storeConfigFile);
            write.close();
        }
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getValueSet method, of class Config.
     */
    @Test
    public void testGetValueSet() throws Exception{
        System.out.println("getValueSet");

        List<String> result = Config.getValueSet("StateVariable");
        
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

        result = Config.getValueSet("do not exist");

        assertNull(result);
    }

    /**
     * Test of getValue method, of class Config.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
                
        String result = Config.getValue("StateCacheSize");
        
        assertEquals("100", result);
        
        result = Config.getValue("do not exist");
        
        assertNull(result);
    }
    @Test
    public void testGetXMLfirstline(){
        System.out.println("getXMLfirstline");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", Config.getXMLfirstline());
    }
}

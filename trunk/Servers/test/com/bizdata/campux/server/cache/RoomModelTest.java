/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.cache;

import org.junit.Ignore;
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
public class RoomModelTest {
    
    public RoomModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        rm = new RoomModel("./users/", 2, 2000);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    static RoomModel rm;
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void test1() {
        System.out.println("appendPiece");
        
        boolean result = rm.appendPiece("room1", "test content1");
        assertTrue(result);
        
        result = rm.appendPiece("room1", "test content2");
        assertTrue(result);
        
        result = rm.appendPiece("room1", "test content3");
        assertTrue(result);
        
        result = rm.appendPiece("room2", "test content4");
        assertTrue(result);
        
        result = rm.appendPiece("room3", "test content5");
        assertTrue(result);
    }
    
    /**
     * Test of readPieces method, of class RoomModel.
     */
    @Test
    public void testReadPieces() {
        System.out.println("readPieces");
        
        List<Piece> list = rm.readPieces("room1");
        for(Piece p:list){
            System.out.println(p.m_content + " " + p.m_timestamp + " " + p.block);
        }
        
        list = rm.readPieces("room2");
        for(Piece p:list){
            System.out.println(p.m_content + " " + p.m_timestamp + " " + p.block);
        }
        
        list = rm.readPieces("room3");
        for(Piece p:list){
            System.out.println(p.m_content + " " + p.m_timestamp + " " + p.block);
        }
        
    }
    
    @Test
    public void testReadPieces_Afterawhile() throws Exception{
        Thread.sleep(2000);
        
        System.out.println("readPieces again");
        
        List<Piece> list = rm.readPieces("room1");
        for(Piece p:list){
            System.out.println(p.m_content + " " + p.m_timestamp + " " + p.block);
        }
        
        list = rm.readPieces("room2");
        for(Piece p:list){
            System.out.println(p.m_content + " " + p.m_timestamp + " " + p.block);
        }
        
        list = rm.readPieces("room3");
        for(Piece p:list){
            System.out.println(p.m_content + " " + p.m_timestamp + " " + p.block);
        }
        
    }
}

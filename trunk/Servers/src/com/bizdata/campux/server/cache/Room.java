/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.cache;

import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.Log.Type;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author yuy
 * @date 2012-02-14 01:31:22
 */
public class Room implements java.io.Serializable{
    public LinkedList<Piece> m_pieces;
    public String m_name;
    public boolean m_unsaved = false;
    protected String m_file;
    
    /**
     * 添加一个条目
     * @return 
     */
    public void addPiece(Piece p){
        p.m_timestamp = System.currentTimeMillis();
        if( p.m_content==null )
            p.m_content="";
        m_pieces.addFirst(p);
        m_unsaved = true;
        flush();
    }
    
    /**
     * 清除过时条目
     * @param time 
     */
    public void timeOut(long time){
        for(Iterator<Piece> itr = m_pieces.iterator(); itr.hasNext();){
            if( time > itr.next().m_timestamp ){
                itr.remove();
                m_unsaved = true;
            }
        }
        // time out do not flush
    }
    
    /**
     * 按时间删除条目
     * @param time 
     */
    public void remove(long time){
        for(Piece piece:m_pieces){
            if( piece.m_timestamp == time ){
                piece.block = true;
                m_unsaved = true;
            }
        }
        flush();
    }
    /**
     * 按内容删除条目
     * @param content 
     */
    public void remove(String content){
        if( content==null ){
            for(Piece piece:m_pieces){
                if( piece.m_content == null ){
                    piece.block = true;
                    m_unsaved = true;
                }
            }            
        }else{
            for(Piece piece:m_pieces){
                if( piece.m_content.equals(content) ){
                    piece.block = true;
                    m_unsaved = true;
                }
            }
        }
        flush();
    }
    
    public void saveRoom(){
        try{
            ObjectOutputStream output = new ObjectOutputStream( new BufferedOutputStream(new FileOutputStream(m_file)));
            output.writeObject(this.m_name);
            output.writeObject(this.m_pieces);
            output.close();
        }catch(Exception exc)
        {
            Log.log("Room", Type.NOTICE, exc);
        }
    }
    
    public void flush(){
        if( m_unsaved ){
            m_unsaved=false;
            saveRoom();
        }
    }
    
    
    static public Room loadRoom(String filepath, String name){
        File file = new File(filepath + name + ".room");
        if( !file.exists() )
            return null;
        
        Room room = new Room();
        room.m_file = filepath+name+".room";
        room.m_name = name;
        try{
            ObjectInputStream input = new ObjectInputStream( new BufferedInputStream(new FileInputStream(filepath+name+".room")));
            room.m_name = (String)input.readObject();
            room.m_pieces = (LinkedList<Piece>)input.readObject();
            input.close();
            room.m_file = filepath + name + ".room";
            room.m_unsaved = false;
        }catch(Exception exc)
        {
            Log.log("Room", Type.NOTICE, exc);
            return null;
        }
        return room;
    }
    
    static public Room loadOrCreateRoom(String filepath, String name){
        File file = new File(filepath + name + ".room");
        if( file.exists() ){
            return loadRoom(filepath, name);
        }else{
            return newRoom(filepath, name);
        }
    }
    
    static public Room newRoom(String filepath, String name){
        File file = new File(filepath+name+".room");
        if( file.exists() )
            return null;
        
        Room room = new Room();
        room.m_file = filepath+name+".room";
        room.m_name = name;
        room.m_unsaved = true;
        room.m_pieces = new LinkedList<Piece>();
        return room;
    }
}

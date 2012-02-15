/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.cache;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author yuy
 * @date 2012-02-13 10:38:17
 */
public class RoomModel {
    // path to store files
    protected String m_path;
    // lift time of messages
    protected long m_lifeTime;
    // message box catch size
    protected int m_cachenumRoom = 100;
    // cache of rooms
    protected Cache<String, Room> m_cacheRoom;
    
    public RoomModel(String path, int cachenumRoom, long lifetime){
        m_path = path;
        if( !m_path.endsWith("\\") && !m_path.endsWith("/"))
            m_path += "/";
        
        m_lifeTime = lifetime;
        m_cachenumRoom = cachenumRoom;
        m_cacheRoom = new Cache<String, Room>(m_cachenumRoom);
    }
    
    synchronized public List<Piece> readPieces(String roomName){
        Room room = m_cacheRoom.findItem(roomName);
        if( room==null ){
            room = Room.loadRoom(m_path, roomName);
            if( room==null )
                return null; // no such room or error occurs
            m_cacheRoom.cacheItem(roomName, room);
        }
        
        long outdatetime = System.currentTimeMillis() - m_lifeTime;
        room.timeOut(outdatetime);
        
        LinkedList<Piece> list = new LinkedList<Piece>();
        for(Piece p:room.m_pieces){
            if( !p.block )
                list.addLast(p);
        }
        return list;
    }
    
    synchronized public List<Piece> readPieces(List<String> roomNames){
        LinkedList<Piece> fulllist = new LinkedList<Piece>();
        for(String name:roomNames){
            List<Piece> list = readPieces(name);
            if( list!=null)
                fulllist.addAll(list);
        }
        return fulllist;
    }
    
    synchronized public boolean appendPiece(String roomName, String content){
        Room room = m_cacheRoom.findItem(roomName);
        if( room==null ){
            room = Room.loadOrCreateRoom(m_path, roomName);
            if( room==null )
                return false; // error occurs
            m_cacheRoom.cacheItem(roomName, room);
        }
        Piece p = new Piece();
        p.m_content = content;
        room.addPiece(p);
        return true;
    }
    
    synchronized public boolean removePiece(String roomName, String content){
        Room room = m_cacheRoom.findItem(roomName);
        if( room==null ){
            room = Room.loadRoom(m_path, roomName);
            if( room==null )
                return false; // no such room or error occurs
            m_cacheRoom.cacheItem(roomName, room);
        }
        room.remove(content);
        return true;
    }
    
    synchronized public boolean removePiece(String roomName, long time){
        Room room = m_cacheRoom.findItem(roomName);
        if( room==null ){
            room = Room.loadRoom(m_path, roomName);
            if( room==null )
                return false; // no such room or error occurs
            m_cacheRoom.cacheItem(roomName, room);
        }
        room.remove(time);
        return true;
    }
}

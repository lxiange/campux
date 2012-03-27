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

import com.bizdata.campux.sdk.network.ServerCommunicator;
import com.bizdata.campux.sdk.saxhandler.ClassRoomSAX;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author yuy
 * @date 2012-02-20 12:30:09
 */
public class ClassRoom {
    // store login sessionid of user who use service
    protected User m_user = null;
    // store the last access time
    protected long m_lasttime;
    // TCP port of Friend service
    protected int m_ServicePort_Friend;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    // buffered scores
    protected HashMap<String, Integer> m_scores = null;
    
    // initialization, load TCP ports (from a instance of User class)
    public ClassRoom(User user){
    	m_ServicePort_Friend=Integer.parseInt(Config.getValue("ServicePort_ClassSchedule"));
        m_user = user;
    }
    
    public HashMap<String, List<String>> listClassRooms() throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Friend);
        
        ClassRoomSAX sax = new ClassRoomSAX();
        String str = sax.prepareListRooms();
        m_comm.sentString(str);
        
        sax.parseInput(m_comm.getInputStream());
        m_comm.close();
        
        m_scores = sax.getScores();
        
        return sax.getList();
    }
    /**
     * List histories of bubbles from the Bubble Server
     * @param b_initialTime
     * @param b_location
     * @return
     * @throws Exception 
     */
    public boolean publishClassRoomComment(String building, String classroom, boolean good) throws Exception{
        return publishClassRoomComment(building+"_"+classroom, good);
    }
    public boolean publishClassRoomComment(String classroompath, boolean good) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Friend);
        
        ClassRoomSAX sax = new ClassRoomSAX();
        String str = sax.preparePublishInfo(m_user.getSessionID(), good, classroompath);
        m_comm.sentString(str);
        
        sax.parseInput(m_comm.getInputStream());
        m_comm.close();
        
        if( sax.getIsError() ){
            System.out.println(sax.getErrorMsg());
            return false;
        }
        
        if( m_scores!=null ){
            m_scores.remove(classroompath);
        }
        return true;
    }
    public int readClassRoomComment(String building, String classroom) throws Exception{
        return readClassRoomComment(building+"_"+classroom);
    }
    public int readClassRoomComment(String classroompath) throws Exception{
        if( m_scores!=null ){
            Integer val = m_scores.get(classroompath);
            if( val!=null )
                return val.intValue();
        }
        
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Friend);
        
        ClassRoomSAX sax = new ClassRoomSAX();
        String str = sax.prepareReadInfo(m_user.getSessionID(), classroompath);
        m_comm.sentString(str);
        
        sax.parseInput(m_comm.getInputStream());
        m_comm.close();
        
        String val = sax.getResponseString();
        if( val==null || val.isEmpty())
            return -1;
        try{
            int v = Integer.parseInt(val);
            return v;
        }catch(Exception exc){
            return -1;
        }
    }
}
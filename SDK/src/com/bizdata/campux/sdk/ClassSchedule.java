/*
 * Copyright (C) 2011 Nanjing Bizdata-infotech co., ltd.
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
import com.bizdata.campux.sdk.saxhandler.*;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import javax.xml.bind.DatatypeConverter;
import java.io.*;

/**
 * An object of the ClassSchedule class represents a schedule of classes, and implements operations regarding the ClassSchedule service
 * @author gl
 */
public class ClassSchedule{
	// store login sessionid of user who use service
    protected String m_userSessionID = null;
    // store username and password
    protected String m_username, m_userpsw;
    // store the last access time
    protected long m_lasttime;
    // TCP port of ClassSchedule service
    protected int m_ServicePort_ClassSchedule;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    
    // initialization, load TCP ports (from a instance of User class)
    public ClassSchedule(int port,String sessionID){
    	m_ServicePort_ClassSchedule=port;
    	m_userSessionID=sessionID;
    }
    
    /**
     * List classrooms from the ClassSchedule Server
     * @return
     * @throws Exception 
     */
    public String[] classListGet() throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_ClassSchedule);
        
        ClassScheduleSAX schedule = new ClassScheduleSAX();
        String str = schedule.prepareClassListGet();
        m_comm.sentString(str);
        
        schedule.parseInput(m_comm.getInputStream());
        m_comm.close();
        String[] groups = schedule.getList();
        return groups;
    }
    /**
     * Publish information of a classroom from the ClassSchedule Server
     * @param address
     * @param available
     * @return
     * @throws Exception 
     */
    public boolean classPublish(String address,String available)throws Exception{
   	    if( m_comm!=null) m_comm.close();
   	    m_comm = new ServerCommunicator(m_ServicePort_ClassSchedule);
     
   	    ClassScheduleSAX schedule = new ClassScheduleSAX();
        String str = schedule.prepareClassPublish(m_userSessionID, address, available);
        m_comm.sentString(str);
        
        schedule.parseInput(m_comm.getInputStream());
        m_comm.close();        
        if(schedule.getIsError() ) return false;
        
        return true;
    }
    /**
     * Get history of classroom from the ClassSchedule Server
     * @param address
     * @return
     * @throws Exception 
     */
    public String multipeACSet(String address) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_ClassSchedule);
        
        ClassScheduleSAX schedule= new ClassScheduleSAX();
        String str = schedule.prepareClassHistoryRead(m_userSessionID, address);
        
        m_comm.sentString(str);
        schedule.parseInput(m_comm.getInputStream());
        String val = schedule.getResponseString();
        
        m_comm.close();
        
        return val;
    }
    /**
     * Read schedule of a classroom from the ClassSchedule Server
     * @return
     * @throws Exception 
     */
    public String[] classScheduleRead() throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_ClassSchedule);
        
        ClassScheduleSAX schedule = new ClassScheduleSAX();
        String str = schedule.prepareClassScheduleRead(m_userSessionID);
        m_comm.sentString(str);
        
        schedule.parseInput(m_comm.getInputStream());
        m_comm.close();
        String[] groups = schedule.getList();
        return groups;
    }
    /**
     * Set schedule of a classroom from the ClassSchedule Server
     * @param c_day
     * @param c_class
     * @param c_room
     * @param c_content
     * @return
     * @throws Exception 
     */
    public boolean classPublish(String c_day,String c_class,String c_room,String c_content)throws Exception{
   	    if( m_comm!=null) m_comm.close();
   	    m_comm = new ServerCommunicator(m_ServicePort_ClassSchedule);
     
   	    ClassScheduleSAX schedule = new ClassScheduleSAX();
        String str = schedule.prepareClassScheduleSet(m_userSessionID, c_day, c_class, c_room, c_content);
        m_comm.sentString(str);
        
        schedule.parseInput(m_comm.getInputStream());
        m_comm.close();        
        if(schedule.getIsError() ) return false;
        
        return true;
    }
}
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
 * An object of the Friend class represents a alarm clock list of a user, and implements operations regarding the AlarmClock service
 * @author gl
 */
public class AlarmClock{
	// store login sessionid of user who use service
    protected String m_userSessionID = null;
    // store username and password
    protected String m_username, m_userpsw;
    // store the last access time
    protected long m_lasttime;
    // TCP port of AlarmClock service
    protected int m_ServicePort_AlarmClock;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    
    // initialization, load TCP ports (from a instance of User class)
    public AlarmClock(int port,String sessionID){
    	m_ServicePort_AlarmClock=port;
    	m_userSessionID=sessionID;
    }
    
    /**
     * Set a single-day alarm clock from the AlarmClock Server
     * @param c_user
     * @param c_time
     * @param c_content
     * @return
     * @throws Exception 
     */
    public String singleACSet(String c_user,String c_time,String c_content) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_AlarmClock);
        
        AlarmClockSAX clock = new AlarmClockSAX();
        String str = clock.prepareSingleACSet(m_userSessionID, c_user, c_time, c_content);
        
        m_comm.sentString(str);
        clock.parseInput(m_comm.getInputStream());
        String val = clock.getResponseString();
        
        m_comm.close();
        
        return val;
    }
    /**
     * Set a multiple-day alarm clock from the AlarmClock Server
     * @param c_user
     * @param c_time
     * @param c_initialDate
     * @param c_interval
     * @param c_content
     * @return
     * @throws Exception 
     */
    public String multipeACSet(String c_user,String c_time,
			String c_initialDate,String c_interval,String c_content) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_AlarmClock);
        
        AlarmClockSAX clock = new AlarmClockSAX();
        String str = clock.prepareMultipleACSet(m_userSessionID, c_user, c_time, c_initialDate, c_interval, c_content);
        
        m_comm.sentString(str);
        clock.parseInput(m_comm.getInputStream());
        String val = clock.getResponseString();
        
        m_comm.close();
        
        return val;
    }
    /**
     * List alarm clocks from the AlarmClock Server
     * @return
     * @throws Exception 
     */
    public String[] aCEnum() throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_AlarmClock);
        
        AlarmClockSAX clock = new AlarmClockSAX();
        String str = clock.prepareACEnum(m_userSessionID);
        m_comm.sentString(str);
        
        clock.parseInput(m_comm.getInputStream());
        m_comm.close();
        String[] groups = clock.getList();
        return groups;
    }
    /**
     * delete a alarm clock from the AlarmClock Server
     * @param c_alarmID
     * @return
     * @throws Exception 
     */
    public String aCDelete(String c_alarmID) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_AlarmClock);
        
        AlarmClockSAX clock = new AlarmClockSAX();
        String str = clock.prepareACDelete(m_userSessionID, c_alarmID);
        
        m_comm.sentString(str);
        clock.parseInput(m_comm.getInputStream());
        String val = clock.getResponseString();
        
        m_comm.close();
        
        return val;
    }

}
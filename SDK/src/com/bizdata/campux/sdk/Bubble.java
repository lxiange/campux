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
 * An object of the Friend class represents a Bubble list of a user, and implements operations regarding the Bubble service
 * @author gl
 */
public class Bubble {
	// store login sessionid of user who use service
    protected String m_userSessionID = null;
    // store username and password
    protected String m_username, m_userpsw;
    // store the last access time
    protected long m_lasttime;
    // TCP port of Bubble service
    protected int m_ServicePort_Bubble;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    
    // initialization, load TCP ports (from a instance of User class)
    public Bubble(int port,String sessionID){
    	m_ServicePort_Bubble=port;
    	m_userSessionID=sessionID;
    }
    /**
     * Release a bubble from the Bubble Server
     * @param b_location
     * @param b_content
     * @return
     * @throws Exception 
     */
    public boolean bubbleRelease(String b_location,String b_content)throws Exception{
   	    if( m_comm!=null) m_comm.close();
   	    m_comm = new ServerCommunicator(m_ServicePort_Bubble);
     
   	    BubbleSAX bubble = new BubbleSAX();
        String str = bubble.prepareBubbleRelease(m_userSessionID, b_location,b_content);
        m_comm.sentString(str);
        
        bubble.parseInput(m_comm.getInputStream());
        m_comm.close();        
        if(bubble.getIsError() ) return false;
        
        return true;
    }
    /**
     * List histories of bubbles from the Bubble Server
     * @param b_initialTime
     * @param b_location
     * @return
     * @throws Exception 
     */
    public String[] bubbleHistoryRead(String b_initialTime,String b_location) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Bubble);
        
        BubbleSAX bubble = new BubbleSAX();
        String str = bubble.prepareBubbleHistoryRead(m_userSessionID,b_initialTime,b_location);
        m_comm.sentString(str);
        
        bubble.parseInput(m_comm.getInputStream());
        m_comm.close();
        String[] groups = bubble.getList();
        return groups;
    }    
    
    
}
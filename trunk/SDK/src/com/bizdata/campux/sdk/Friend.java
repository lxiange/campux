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
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.io.*;

/**
 * An object of the Friend class represents a friend list of a user, and implements operations regarding the Friend service
 * @author gl
 */
public class Friend {
	 // store login sessionid of user who use service
    protected String m_userSessionID = null;
    // store username and password
    protected String m_username, m_userpsw;
    // store the last access time
    protected long m_lasttime;
    // TCP port of Friend service
    protected int m_ServicePort_Friend;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    
    // initialization, load TCP ports (from a instance of User class)
    public Friend(int port,String sessionID){
    	m_ServicePort_Friend=port;
    	m_userSessionID=sessionID;
    }
    /**
     * List items in Friend table from the Friend Server
     * @return
     * @throws Exception 
     */
    public String[] friendList() throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Friend);
        
        FriendSAX friend = new FriendSAX();
        String str = friend.prepareFriendList(m_userSessionID);
        m_comm.sentString(str);
        
        friend.parseInput(m_comm.getInputStream());
        m_comm.close();
        String[] groups = friend.getList();
        return groups;
    }
    /**
     * Add a item to Friend table from the Friend Server
     * @param friendID
     * @return
     * @throws Exception 
     */
    public boolean friendAdd(String friendID)throws Exception{
   	    if( m_comm!=null) m_comm.close();
   	    m_comm = new ServerCommunicator(m_ServicePort_Friend);
     
        FriendSAX friend = new FriendSAX();
        String str = friend.prepareFriendAdd(m_userSessionID, friendID);
        m_comm.sentString(str);
        
        friend.parseInput(m_comm.getInputStream());
        m_comm.close();        
        if(friend.getIsError() ) return false;
        
        return true;
    }
    /**
     * Delete a item from Friend table from the Friend Server
     * @param friendID
     * @return
     * @throws Exception 
     */
    public boolean friendDelete(String friendID)throws Exception{
   	    if( m_comm!=null) m_comm.close();
   	    m_comm = new ServerCommunicator(m_ServicePort_Friend);
     
        FriendSAX friend = new FriendSAX();
        String str = friend.prepareFriendDelete(m_userSessionID, friendID);
        m_comm.sentString(str);
        
        friend.parseInput(m_comm.getInputStream());
        m_comm.close();        
        if(friend.getIsError() ) return false;
        
        return true;
    }
    /**
     * Change information of Friend table from the Friend Server
     * @param i_content
     * @return
     * @throws Exception 
     */
    public boolean infoChange(String i_content)throws Exception{
   	    if( m_comm!=null) m_comm.close();
   	    m_comm = new ServerCommunicator(m_ServicePort_Friend);
     
        FriendSAX friend = new FriendSAX();
        String str = friend.prepareInfoChange(m_userSessionID, i_content);
        m_comm.sentString(str);
        
        friend.parseInput(m_comm.getInputStream());
        m_comm.close();        
        if(friend.getIsError() ) return false;
        
        return true;
    }
    /**
     * List items of histories of information from the Friend Server
     * @param initialTime
     * @param userName
     * @return
     * @throws Exception 
     */
    public String[] friendList(String initialTime,String userName) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Friend);
        
        FriendSAX friend = new FriendSAX();
        String str = friend.prepareHistoryRead(m_userSessionID, initialTime, userName);
        m_comm.sentString(str);
        
        friend.parseInput(m_comm.getInputStream());
        m_comm.close();
        String[] groups = friend.getList();
        return groups;
    }
    
    
}

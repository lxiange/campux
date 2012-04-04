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
import java.util.List;

/**
 * An object of the Friend class represents a friend list of a user, and implements operations regarding the Friend service
 * @author gl
 */
public class Friend {
    // store login sessionid of user who use service
    protected User m_user = null;
    // store the last access time
    protected long m_lasttime;
    // TCP port of Friend service
    protected int m_ServicePort_Friend;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    
    // initialization, load TCP ports (from a instance of User class)
    public Friend(User user){
    	m_ServicePort_Friend=Integer.parseInt(Config.getValue("ServicePort_Friend"));
        m_user = user;
        m_comm = new ServerCommunicator();
    }
    /**
     * List items in Friend table from the Friend Server
     * @return
     * @throws Exception 
     */
    public List<String> friendRead() {
        return friendRead(m_user.m_username);
    }

    public List<String> friendRead(String username) {
        LinkedList<String> list = new LinkedList<String>();
        String friends =  null;
        try {
            friends = m_user.getUserVariable(username, "Friends");
        } catch (Exception exc) {
        }

        if (friends == null) {
            return null;
        }
        String[] friendsep = friends.split(",");
        
        for (String friendname : friendsep) {
            if( !friendname.isEmpty() )
                list.add(friendname);
        }

        return list;
    }

    public boolean friendAdd(String friendname) {
        return friendAdd(m_user.m_username, friendname);
    }

    public boolean friendAdd(String username, String friendname) {
        
        try {
            boolean exists = false;
            String fstr = "";
            List<String> fnames = friendRead(username);
            for(String fname : fnames){
                if( fname.equals(friendname) )
                    exists = true;
                fstr += "," + fname;
            }
            if( !exists )
                fstr += "," + friendname;
            fstr = fstr.substring(1);
            m_user.setUserVariable(username, "Friends", fstr);
        } catch (Exception exc) {
            return false;
        }
        return true;
    }

    public boolean friendDel(String friendname) {
        return friendDel(m_user.m_username, friendname);
    }

    public boolean friendDel(String username, String friendname) {
        try{
            List<String> list = friendRead(username);
            if( list==null )
                return false;
            String friends="";
            for(String fname : list){
                if( !friendname.equalsIgnoreCase(fname))
                    friends += "," + fname;
            }
            if( friends.length() > 0)
                friends = friends.substring(1);
            m_user.setUserVariable(username, "Friends", friends);
        }catch(Exception exc){
            return false;
        }
        return true;
    }
    
    /**
     * List histories of bubbles from the Bubble Server
     * @param b_initialTime
     * @param b_location
     * @return
     * @throws Exception 
     */
    public FriendMessage[] friendStatusRead(long starttime) throws Exception{
        m_comm.SetupCommunicator(m_ServicePort_Friend);
        
        FriendSAX fsax = new FriendSAX();
        String str = fsax.prepareFriendRead(m_user.getSessionID(), starttime);
        m_comm.sentString(str);
        
        fsax.parseInput(m_comm.getInputStream());
        m_comm.close();
        FriendMessage[] groups = fsax.getList();
        for(FriendMessage msg : groups){
            if( m_lasttime < msg.time )
                m_lasttime = msg.time;
        }
        return groups;
    }
    /**
     * List the update of bubbles
     * @param b_initialTime
     * @param b_location
     * @return
     * @throws Exception 
     */
    public FriendMessage[] friendStatusUpdate() throws Exception{
        return friendStatusRead(m_lasttime);
    }
    
    public boolean __friendStatusPublish(String targetuser, String content) throws Exception{
        m_comm.SetupCommunicator(m_ServicePort_Friend);

        FriendSAX fsax = new FriendSAX();
        String str = fsax.prepareFriendPublish(m_user.getSessionID(), targetuser, content);
        
        m_comm.sentString(str);
        
        fsax.parseInput(m_comm.getInputStream());
        m_comm.close();
        if(fsax.getIsError() ) return false;
        
        return true;
    }
    
}

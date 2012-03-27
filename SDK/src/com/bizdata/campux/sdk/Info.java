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
public class Info {
    // store login sessionid of user who use service
    protected User m_user = null;
    // store the last access time
    protected long m_lasttime;
    // TCP port of Friend service
    protected int m_ServicePort_Info;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    
    // initialization, load TCP ports (from a instance of User class)
    public Info(User user){
    	m_ServicePort_Info=Integer.parseInt(Config.getValue("ServicePort_UserInfo"));
        m_user = user;
    }
    /**
     * List items in Friend table from the Friend Server
     * @return
     * @throws Exception 
     */
    public List<String> infoRoomRead() {
        return infoRoomRead(m_user.m_username);
    }
    public List<String> infoRoomRead(String username) {
        return _varRead("InfoRooms", username);
    }
    public List<String> infoPublisherRead() {
        return infoPublisherRead(m_user.m_username);
    }
    public List<String> infoPublisherRead(String username) {
        return _varRead("InfoPublishers", username);
    }
    public boolean infoRoomAdd(String room) {
        return infoRoomAdd(m_user.m_username, room);
    }
    public boolean infoRoomAdd(String username, String room) {
        return _varAdd("InfoRooms", username, room);
    }
    public boolean infoPublisherAdd(String room) {
        return infoPublisherAdd(m_user.m_username, room);
    }
    public boolean infoPublisherAdd(String username, String room) {
        return _varAdd("InfoPublishers", username, room);
    }
    public boolean infoRoomDel(String room) {
        return infoRoomDel(m_user.m_username, room);
    }
    public boolean infoRoomDel(String username, String room) {
        return _varDel("InfoRooms", username, room);
    }
    public boolean infoPublisherDel(String room) {
        return infoPublisherDel(m_user.m_username, room);
    }
    public boolean infoPublisherDel(String username, String room) {
        return _varDel("InfoPublishers", username, room);
    }
    
    protected List<String> _varRead(String varname, String username) {
        LinkedList<String> list = new LinkedList<String>();
        String varstr =  null;
        try {
            varstr = m_user.getUserVariable(username, varname);
        } catch (Exception exc) {
        }

        if (varstr == null) {
            return null;
        }
        String[] varsep = varstr.split(",");
        
        for (String var : varsep) {
            if( !var.isEmpty() )
                list.add(var);
        }

        return list;
    }
    protected boolean _varDel(String varname, String username, String delitem) {
        try{
            List<String> list = _varRead(varname, username);
            if( list==null )
                return false;
            String varstr="";
            for(String fname : list){
                if( !delitem.equalsIgnoreCase(fname))
                    varstr += "," + fname;
            }
            if( varstr.length() > 0)
                varstr = varstr.substring(1);
            m_user.setUserVariable(username, varname, varstr);
        }catch(Exception exc){
            return false;
        }
        return true;
    }
    
    protected boolean _varAdd(String varname, String username, String additem) {        
        try {
            boolean exists = false;
            String fstr = "";
            List<String> varlist = _varRead(varname, username);
            for(String var : varlist){
                if( var.equals(additem) )
                    exists = true;
                fstr += "," + var;
            }
            if( !exists )
                fstr += "," + additem;
            fstr = fstr.substring(1);
            m_user.setUserVariable(username, varname, fstr);
        } catch (Exception exc) {
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
    public InfoMessage[] infoRead(long starttime) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Info);
        
        UserInfoSAX sax = new UserInfoSAX();
        String str = sax.prepareInfoRead(m_user.getSessionID(), starttime);
        m_comm.sentString(str);
        
        sax.parseInput(m_comm.getInputStream());
        m_comm.close();
        InfoMessage[] groups = sax.getList();
        for(InfoMessage msg : groups){
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
    public InfoMessage[] infoUpdate() throws Exception{
        return infoRead(m_lasttime);
    }
    
    public boolean __infoPublish(String roomname, String content) throws Exception{
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Info);

        UserInfoSAX fsax = new UserInfoSAX();
        String str = fsax.prepareInfoPublish(m_user.getSessionID(), roomname, content);
        
        m_comm.sentString(str);
        
        fsax.parseInput(m_comm.getInputStream());
        m_comm.close();
        if(fsax.getIsError() ) return false;
        
        return true;
    }
    
}

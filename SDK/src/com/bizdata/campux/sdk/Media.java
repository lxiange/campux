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
 * An object of the Friend class represents a Media list of a user, and implements operations regarding the Media service
 * @author gl
 */
public class Media{
	// store login sessionid of user who use service
    protected String m_userSessionID = null;
    // store username and password
    protected String m_username, m_userpsw;
    // store the last access time
    protected long m_lasttime;
    // TCP port of Media service
    protected int m_ServicePort_Media;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    
    // initialization, load TCP ports (from a instance of User class)
    public Media(int port,String sessionID){
    	m_ServicePort_Media=port;
    	m_userSessionID=sessionID;
    }
    
    /**
     * Get media list from the Media Server
     * @return
     * @throws Exception 
     */
    public String[] mediaListGet() throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Media);
        
        MediaSAX media = new MediaSAX();
        String str = media.prepareMediaListGet(m_userSessionID);
        m_comm.sentString(str);
        
        media.parseInput(m_comm.getInputStream());
        m_comm.close();
        String[] groups = media.getList();
        return groups;
    }
    /**
     * Get media content from the Media Server
     * @param m_content
     * @return
     * @throws Exception 
     */
    public String[] mediaContentRead(String m_content) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Media);
        
        MediaSAX media = new MediaSAX();
        String str = media.prepareMediaContentRead(m_userSessionID, m_content);
        m_comm.sentString(str);
        
        media.parseInput(m_comm.getInputStream());
        m_comm.close();
        String[] groups = media.getList();
        return groups;
    }
    /**
     * Get music from the ClassSchedule Server
     * @param m_ID
     * @return
     * @throws Exception 
     */
    public String musicRead(String m_ID) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Media);
        
        MediaSAX media= new MediaSAX();
        String str = media.prepareMusicRead(m_userSessionID, m_ID);
        
        m_comm.sentString(str);
        media.parseInput(m_comm.getInputStream());
        String val = media.getResponseString();
        
        m_comm.close();
        
        return val;
    }
}
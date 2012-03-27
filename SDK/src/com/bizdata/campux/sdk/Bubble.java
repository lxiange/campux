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


/**
 * An object of the Friend class represents a Bubble list of a user, and implements operations regarding the Bubble service
 * @author gl
 */
public class Bubble {
    // store login sessionid of user who use service
    protected User m_user;
    // store the last access time
    protected long m_lasttime = 0;
    // TCP port of Bubble service
    protected int m_ServicePort_Bubble;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    
    
    // initialization, load TCP ports (from a instance of User class)
    public Bubble(User user){
    	m_ServicePort_Bubble=Integer.parseInt(Config.getValue("ServicePort_Bubble"));
        m_user = user;
    }
    /**
     * Release a bubble from the Bubble Server
     * @param b_location
     * @param b_content
     * @return
     * @throws Exception 
     */
    public boolean publish(String content)throws Exception{
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Bubble);

        BubbleSAX bubble = new BubbleSAX();
        String str = bubble.prepareBubblePublish(m_user.getSessionID(), content);
        
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
    public BubbleMessage[] bubbleRead(long starttime) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_Bubble);
        
        BubbleSAX bubble = new BubbleSAX();
        String str = bubble.prepareBubbleRead(m_user.getSessionID(), starttime);
        m_comm.sentString(str);
        
        bubble.parseInput(m_comm.getInputStream());
        m_comm.close();
        BubbleMessage[] groups = bubble.getList();
        for(BubbleMessage msg : groups){
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
    public BubbleMessage[] bubbleUpdate() throws Exception{
        return bubbleRead(m_lasttime);
    }
    
}
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
import com.bizdata.campux.sdk.saxhandler.UserAuthSAX;
import com.bizdata.campux.sdk.saxhandler.UserStatusSAX;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import javax.xml.bind.DatatypeConverter;


/**
 * An object of the User class represents a user, and implements operations regarding the user
 * @author yuy
 */
public class User{
    // store login sessionid
    protected String m_userSessionID = null;
    // store username and password
    protected String m_username, m_userpsw;
    // store the last access time
    protected long m_lasttime;
    // TCP port of UserAuth service
    protected int m_ServicePort_UserAuth;
    // TCP port of UserStatus service
    protected int m_ServicePort_UserStatus;
    // object for communication with the server
    protected ServerCommunicator m_comm = null;
    // is the user an APP user
    protected boolean m_isapp = false;
    
    // initialization, load TCP ports
    public User(){
        m_ServicePort_UserAuth = Integer.parseInt(Config.getValue("ServicePort_UserAuth"));
        m_ServicePort_UserStatus = Integer.parseInt(Config.getValue("ServicePort_UserStatus"));
    }
    
    /**
     * login a user of APP. There will be no login timeout
     * @param name
     * @param psw
     * @return
     * @throws Exception 
     */
    public boolean loginAPP(String name, String psw) throws Exception{
        m_isapp = true;
        return login(name, psw);
    }
    
    /**
     * login a user on the UserAuth Server
     * @param name username
     * @param psw password
     * @return true if the login is ok
     */
    public boolean login(String name, String psw) throws Exception{
        m_username = name;
        m_userpsw = psw;
        
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_UserAuth);
        
        UserAuthSAX auth = new UserAuthSAX();
        String str = auth.prepareLogin(m_username, m_userpsw);
        m_comm.sentString(str);
        
        auth.parseInput(m_comm.getInputStream());
        
        if( auth.getIsError() ){
            m_userSessionID=null;
            return false;
        }
        
        String id = auth.getResponseString();
        
        m_comm.close();
        
        m_userSessionID = id;
        
        m_lasttime = System.currentTimeMillis();
        return true;
    }
    /**
     * logout from the UserAuth Server
     */
    public void logout() throws Exception{
        //TODO: implement the logout function on the server
        if( m_userSessionID == null)
            return;
        
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_UserAuth);
        
        UserAuthSAX auth = new UserAuthSAX();
        String str = auth.prepareLogout(m_userSessionID);
        m_comm.sentString(str);
        
        m_comm.close();
        
        m_userSessionID = null;
        m_username = null;
        m_userpsw = null;
    }
    /**
     * register a new user on the UserAuth Server
     * @param studentID the studentID is the system username
     * @param psw user password
     * @param name user's real and display name
     * @param depart user's department
     * @param school user's school
     * @param grade user's grade
     * @param age user's age
     * @param gender user's gender
     * @return true if the registration is ok
     */
    public boolean register(String studentID, String psw, String name, 
            String depart, String school, String grade, String age,
            String gender) throws  Exception{
        
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_UserAuth);
        
        UserAuthSAX auth = new UserAuthSAX();
        String str = auth.prepareRegistration(studentID, psw);
        m_comm.sentString(str);
        
        auth.parseInput(m_comm.getInputStream());
        m_comm.close();        
        if( auth.getIsError() ) return false;
        
        // login
        if( !login(studentID, psw) ) return false;
        // set variables
        if(name!=null) if( !setUserVariable("UserName", name) ) return false;
        if(depart!=null) if( !setUserVariable("UserDepartment", depart) ) return false;
        if(school!=null) if( !setUserVariable("UserSchool", school) ) return false;
        if(grade!=null) if( !setUserVariable("UserGrade", grade) ) return false;
        if(age!=null) if( !setUserVariable("UserAge", age) ) return false;
        if(gender!=null) if( !setUserVariable("UserGender", gender) ) return false;
        return true;
    }
    /**
     * lookup the username through userSessionID on the UserAuth Server
     * @param userSessionID obtained by login
     * @return username
     */
    public String lookupUsername(String userSessionID) {
        String user = null;
        try{
            // force shutdown of the old connection
            if( m_comm!=null) m_comm.close();
            m_comm = new ServerCommunicator(m_ServicePort_UserAuth);
            
            UserAuthSAX auth = new UserAuthSAX();
            String str=auth.prepareLookup(userSessionID);
            m_comm.sentString(str);       
            
            auth.parseInput(m_comm.getInputStream());

            if( auth.getIsError() ){
                m_userSessionID=null;
                return null;
            }

            user = auth.getResponseString();

            m_comm.close();
        }catch(Exception exc){
            return null;
        }
        return user;
    }
    /**
     * get the current userSessionID on the UserAuth Server. 
     * The ID is get refreshed if session timeouts
     * @return current UserSessionID 
     */
    public String getSessionID() throws Exception{
        long timeleft = System.currentTimeMillis() - m_lasttime;
        if( !m_isapp && timeleft >= 3600000L)
            login(m_username, m_userpsw);
        return m_userSessionID;
    }
    
    /**
     * List user variables from the UserStatus Server
     * @return
     * @throws Exception 
     */
    public String[] getUserVariables() throws Exception{        
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_UserStatus);
        // SAX object
        UserStatusSAX status = new UserStatusSAX();
        // prepare the communication string
        String str = status.prepareUserVariables();
        // send the query
        m_comm.sentString(str);
        // parse the input
        status.parseInput(m_comm.getInputStream());
        // get the variables
        String[] vars = status.getVariables();
        // close the connection
        m_comm.close();
        
        return vars;
    }
    /**
     * Get the value of a varibale from the UserStatus Server
     * @param var
     * @return
     * @throws Exception 
     */
    public String getUserVariable(String var) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_UserStatus);
        
        UserStatusSAX status = new UserStatusSAX();
        String str = status.prepareGetUserVariable(getSessionID(), var);
        
        m_comm.sentString(str);
        status.parseInput(m_comm.getInputStream());
        String val = status.getValue();
        
        m_comm.close();
        
        return val;
    }
    /**
     * Set a user variable on the UserStatus Server
     * @param var
     * @param val
     * @return
     * @throws Exception 
     */
    public boolean setUserVariable(String var, String val) throws Exception{
        // force shutdown of the old connection
        if( m_comm!=null) m_comm.close();
        m_comm = new ServerCommunicator(m_ServicePort_UserStatus);
        
        UserStatusSAX status = new UserStatusSAX();
        String str = status.prepareSetUserVariable(getSessionID(), var, val);
        
        m_comm.sentString(str);
        
        status.parseInput(m_comm.getInputStream());
        boolean success = !status.getIsError();
        
        m_comm.close();
        return success;
    }
    
}

package com.bizdata.campux.sdk;

import com.bizdata.campux.sdk.network.ServerCommunicator;
import com.bizdata.campux.sdk.saxhandler.UserStatusSAX;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;


/**
 *
 * @author yuy
 */
public class User{
    // store login sessionid
    protected String m_userSessionID = null;
    // store username and password
    protected String m_username, m_userpsw;
    // store the last access time
    protected long m_lasttime;
    protected int m_ServicePort_UserAuth;
    protected int m_ServicePort_UserStatus;
    public User(){
        m_ServicePort_UserAuth = Integer.parseInt(Config.getValue("ServicePort_UserAuth"));
        m_ServicePort_UserStatus = Integer.parseInt(Config.getValue("ServicePort_UserStatus"));
    }
    /**
     * login a user
     * @param name username
     * @param psw password
     * @return true if the login is ok
     */
    public boolean login(String name, String psw){
        m_username = name;
        m_userpsw = psw;
        m_userSessionID = "0000123";
        m_lasttime = System.currentTimeMillis();
        return true;
    }
    /**
     * logout
     */
    public void logout(){
        if( m_userSessionID == null)
            return;
        m_userSessionID = null;
        
    }
    /**
     * register a new user
     * @param name user name
     * @param psw user password
     * @param studentID user's student ID
     * @param depart user's department
     * @param school user's school
     * @param grade user's grade
     * @param age user's age
     * @param gender user's gender
     * @param photo user's face photo encoded in a string (using base64 encoding)
     * @return true if the registration is ok
     */
    public boolean register(String name, String psw, String studentID, 
            String depart, String school, String grade, String age,
            String gender, String photo){
        return true;
    }
    /**
     * lookup the username through userSessionID
     * @param userSessionID obtained by login
     * @return username
     */
    public String lookupUsername(String userSessionID){
        return "notexistuser";
    }
    /**
     * get the current userSessionID. The ID is get refreshed if session timeouts
     * @return current UserSessionID 
     */
    public String getSessionID(){
        long timeleft = System.currentTimeMillis() - m_lasttime;
        if( timeleft >= 3600000L)
            login(m_username, m_userpsw);
        return m_userSessionID;
    }
    public String[] getUserVariables() throws Exception{        
        ServerCommunicator comm = new ServerCommunicator(m_ServicePort_UserStatus);
        OutputStreamWriter output = new OutputStreamWriter(comm.getOutputStream());
        output.write(Config.getXMLfirstline());
        output.write("<usl></ucl>");
        output.flush();
        
        UserStatusSAX status = new UserStatusSAX();
        status.parseInput(comm.getInputStream());
        String[] vars = status.getVariables();
        
        comm.close();
        
        return vars;
    }
    
}

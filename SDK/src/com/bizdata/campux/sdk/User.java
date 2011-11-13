package com.bizdata.campux.sdk;


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
    public String[] getUserVariables(){
        
    }
}

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
    protected ServerCommunicator m_comm = null;
    public User(){
        m_ServicePort_UserAuth = Integer.parseInt(Config.getValue("ServicePort_UserAuth"));
        m_ServicePort_UserStatus = Integer.parseInt(Config.getValue("ServicePort_UserStatus"));
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
        OutputStreamWriter output = new OutputStreamWriter(m_comm.getOutputStream());
        //output.write(Config.getXMLfirstline());
        output.write("<v><u>");
        output.write(m_username);
        output.write("</u><p>");
        output.write(m_userpsw);
        output.write("</p></v>\r\n");
        output.flush();
        
        UserAuthSAX auth = new UserAuthSAX();
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
    public void logout(){
        //TODO: implement the logout function on the server
        if( m_userSessionID == null)
            return;
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
        OutputStreamWriter output = new OutputStreamWriter(m_comm.getOutputStream());
        //output.write(Config.getXMLfirstline());
        output.write("<a><u>" + studentID + "</u><p>" + psw + "</p></a>\r\n");
        output.flush();
        
        UserAuthSAX auth = new UserAuthSAX();
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
            OutputStreamWriter output = new OutputStreamWriter(m_comm.getOutputStream());
            //output.write(Config.getXMLfirstline());
            output.write("<c><s>" + userSessionID + "</s></c>\r\n");
            output.flush();
        
            UserAuthSAX auth = new UserAuthSAX();
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
        if( timeleft >= 3600000L)
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
        OutputStreamWriter output = new OutputStreamWriter(m_comm.getOutputStream());
        output.write(Config.getXMLfirstline());
        output.write("<usl></usl>");
        output.flush();
        
        UserStatusSAX status = new UserStatusSAX();
        status.parseInput(m_comm.getInputStream());
        String[] vars = status.getVariables();
        
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
        OutputStreamWriter output = new OutputStreamWriter(m_comm.getOutputStream());
        output.write(Config.getXMLfirstline());
        String outputstr = "<usr s=\"" + getSessionID() + "\">" + var + "</usr>";
        output.write(outputstr);
        if(Config.debug())
            System.out.println("output: "+ outputstr);
        output.flush();
        
        UserStatusSAX status = new UserStatusSAX();
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
        OutputStreamWriter output = new OutputStreamWriter(m_comm.getOutputStream());
        output.write(Config.getXMLfirstline());
        
        val = DatatypeConverter.printBase64Binary(val.getBytes(Config.getCharset()));
        String outputstr = "<usw s=\"" + getSessionID() + "\" n=\"" + var + "\" b64=\"true\">" + val + "</usw>";
        output.write(outputstr);
        if(Config.debug())
            System.out.println("output: "+ outputstr);
        output.flush();
        
        UserStatusSAX status = new UserStatusSAX();
        status.parseInput(m_comm.getInputStream());
        boolean success = !status.getIsError();
        
        m_comm.close();
        return success;
    }
    
}

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
import com.bizdata.campux.sdk.saxhandler.LocationSAX;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author yuy
 * @date 2011-12-22 02:47:47
 */
public class Locator {
    protected HashMap<String, Integer> m_wifis = new HashMap<String, Integer>();
    protected String m_connected;
    protected String m_ip;
    protected ServerCommunicator m_comm;
    protected int m_ServicePort_Locator;
    
    public Locator(){
        try{
            if( Config.needInit() ){
    		InputStream input=new FileInputStream("sdk.config");
    		Config.init(input);
            }
    	}
 	catch(Exception e){System.out.println("Can't find sde.config!");}
        m_ServicePort_Locator = Integer.parseInt(Config.getValue("ServicePort_WifiLocator"));
        m_comm = new ServerCommunicator();
    }
    
    public void clean(){
        m_ip = null;
        m_connected = null;
        m_wifis.clear();
    }
    public void addWifi(String bssid, int strength, boolean connected){
        m_wifis.put(bssid, strength);
        if( connected )
            m_connected = bssid;
    }
    public void setIP(String ip){
        m_ip = ip;
    }
    public String getLocation(User user) throws Exception{
        m_comm.SetupCommunicator(m_ServicePort_Locator);
       
        LocationSAX locator = new LocationSAX();
        String str = locator.prepareGetLocation(user.m_userSessionID, m_ip, m_wifis, m_connected);
        m_comm.sentString(str);
        locator.parseInput(m_comm.getInputStream());
        m_comm.close();
        if( locator.getIsError() ) return null;
        return locator.getResponseString();
    }
    public void setLocation(User user, String locationname) throws Exception{
        m_comm.SetupCommunicator(m_ServicePort_Locator);
       
        LocationSAX locator = new LocationSAX();
        String str = locator.prepareAddLocation(user.m_userSessionID, locationname, m_ip, m_wifis, m_connected);
        m_comm.sentString(str);
        locator.parseInput(m_comm.getInputStream());
        m_comm.close();
        if( locator.getIsError() )
            Log.log("wifi", Log.Type.ERROR, "add location fail.");
        return;
    }
}

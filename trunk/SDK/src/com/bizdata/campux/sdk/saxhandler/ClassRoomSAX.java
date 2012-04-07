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
package com.bizdata.campux.sdk.saxhandler;

import com.bizdata.campux.sdk.Config;
import com.bizdata.campux.sdk.FriendMessage;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author yuy
 */
public class ClassRoomSAX extends SAXHandlerBase {

    protected HashMap<String, List<String>> m_vars = new HashMap<String, List<String>>();
    protected HashMap<String, Integer> m_scores = new HashMap<String, Integer>();
    protected String m_responseStr = null;

    public String getResponseString() {
        return m_responseStr;
    }

    public HashMap<String, List<String>> getList() {
        return m_vars;
    }
    
    public HashMap<String, Integer> getScores(){
        return m_scores;
    }
    
    

    @Override
    protected void contentReceived(String content, String tagname) {
        System.out.println("contentrecevied:" + tagname);
        if ("ok".equalsIgnoreCase(tagname)) {
            m_responseStr = content;
        } else if ("c".equalsIgnoreCase(tagname)) {
            
            String building = content.substring(0, content.indexOf("_"));
            String roomname = content.substring(content.indexOf("_")+1);
            
            String scorestr = m_moreatt.get("m");
            int score = -1;
            try{
                score = Integer.parseInt(scorestr);
            }catch(Exception exc){ }
            
            List<String> buildingrooms = m_vars.get(building);
            if( buildingrooms==null ){
                buildingrooms = new LinkedList<String>();
                m_vars.put(building, buildingrooms);
            }
            buildingrooms.add(roomname);
            
            m_scores.put(content, score);
        }
    }

    /**
     * this function is for the system. not for the client.
     * @param sessionID
     * @param content
     * @return 
     */
    public String prepareListRooms() {
        return "<lc></lc>";
    }

    public String preparePublishInfo(String sessionID, boolean good, String location) {
        StringBuilder str = new StringBuilder();
        str.append("<pr ");
        str.append("s=\"" + sessionID + "\" ");
        str.append("c=\"" + (good?"1":"0") + "\" b64=\"true\">");
        
        String b64 = DatatypeConverter.printBase64Binary(location);
        
        str.append(b64);
        
        str.append("</pr>");
        return str.toString();
    }
    
    public String prepareReadInfo(String sessionID, String location) {
        StringBuilder str = new StringBuilder();
        str.append("<rr ");
        str.append("s=\"" + sessionID + "\" b64=\"true\">");
        
        String b64 = DatatypeConverter.printBase64Binary(location);
        
        str.append(b64);
        
        str.append("</rr>");
        return str.toString();
    }

    /*   public static void main(String[] args){
    String sessionID="1234556677";
    FriendSAX ff=new FriendSAX();
    System.out.println(ff.prepareFriendList(sessionID));
    System.out.println(ff.prepareFriendAdd(sessionID, "aa"));
    System.out.println(ff.prepareFriendDelete(sessionID, "bb"));
    System.out.println(ff.prepareInfoChange(sessionID, "cc"));
    System.out.println(ff.prepareHistoryRead(sessionID, "dd", "ee"));
    }
     */
}

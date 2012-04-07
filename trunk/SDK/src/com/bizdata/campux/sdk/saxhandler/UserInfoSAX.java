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
import com.bizdata.campux.sdk.InfoMessage;
import com.bizdata.campux.sdk.util.DatatypeConverter;
import java.io.InputStream;
import java.util.LinkedList;
import org.xml.sax.Attributes;

/**
 * SAX handler
 * @author yuy
 */
public class UserInfoSAX extends SAXHandlerBase{

    protected LinkedList<InfoMessage> m_vars = new LinkedList<InfoMessage>();
    protected String m_responseStr = null;

    public String getResponseString() {
        return m_responseStr;
    }

    public InfoMessage[] getList() {
        InfoMessage[] vs = new InfoMessage[m_vars.size()];
        for (int i = 0; i < vs.length; i++) {
            vs[i] = m_vars.get(i);
        }
        return vs;
    }

    @Override
    protected void contentReceived(String content, String tagname) {
        System.out.println("contentrecevied:" + tagname);
        if ("ok".equalsIgnoreCase(tagname)) {
            m_responseStr = content;
        } else if ("s".equalsIgnoreCase(tagname)) {
            InfoMessage msg = new InfoMessage();
            msg.message = content;
            msg.publisher = m_moreatt.get("u");
            msg.time = Long.parseLong(m_moreatt.get("d"));
            m_vars.add(msg);
        }
    }

    /**
     * this function is for the system. not for the client.
     * @param sessionID
     * @param content
     * @return 
     */
    public String prepareInfoPublish(String sessionID, String room, String content) {
        String val = DatatypeConverter.printBase64Binary(content);

        StringBuilder str = new StringBuilder();
        str.append("<si ");
        str.append("s=\"" + sessionID + "\" r=\"");
        str.append(room);
        str.append("\" b64=\"true\">");
        str.append(val);
        str.append("</si>");
        return str.toString();
    }

    public String prepareInfoRead(String sessionID, long time) {
        StringBuilder str = new StringBuilder();
        str.append("<gs ");
        str.append("s=\"" + sessionID + "\" ");
        str.append("d=\"" + time + "\">");
        str.append("</gs>");
        return str.toString();
    }

    
 /*   public static void main(String[] args){
    	UserInfoSAX ui=new UserInfoSAX();
    	String sessionID="1234556677";
    	String infoID="INFOA";
    	String[] pt={"xx","yy","zz"};
    	System.out.println(ui.preparePublisherList());
    	System.out.println(ui.prepareAccountInitialization(sessionID));
    	System.out.println(ui.prepareUpdateCheck(sessionID));
    	System.out.println(ui.prepareInfoAchieve(sessionID,infoID));
    	System.out.println(ui.prepareInfoDelete(sessionID,infoID));
    	System.out.println(ui.preparePublisherRegistration(sessionID,"a","b","c",pt));
    	System.out.println(ui.prepareInfoPublish(sessionID,"a","b","c","d","e","f","g","h"));
    }
*/
}



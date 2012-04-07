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
import java.util.LinkedList;

/**
 *
 * @author yuy
 */
public class FriendSAX extends SAXHandlerBase {

    protected LinkedList<FriendMessage> m_vars = new LinkedList<FriendMessage>();
    protected String m_responseStr = null;

    public String getResponseString() {
        return m_responseStr;
    }

    public FriendMessage[] getList() {
        FriendMessage[] vs = new FriendMessage[m_vars.size()];
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
        } else if ("m".equalsIgnoreCase(tagname)) {
            FriendMessage msg = new FriendMessage();
            msg.type = content.charAt(0)-'0';
            msg.message = content.substring(1);
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
    public String prepareFriendPublish(String sessionID, String targetuser, String content) {
        String val = DatatypeConverter.printBase64Binary(content);

        StringBuilder str = new StringBuilder();
        str.append("<ps ");
        str.append("s=\"" + sessionID + "\" u=\"");
        str.append(targetuser);
        str.append("\" b64=\"true\">");
        str.append(val);
        str.append("</ps>");
        return str.toString();
    }

    public String prepareFriendRead(String sessionID, long time) {
        StringBuilder str = new StringBuilder();
        str.append("<rs ");
        str.append("s=\"" + sessionID + "\" ");
        str.append("d=\"" + time + "\">");
        str.append("</rs>");
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

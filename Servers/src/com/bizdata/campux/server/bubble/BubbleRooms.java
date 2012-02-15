/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.bubble;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.cache.RoomModel;

/**
 *
 * @author yuy
 * @date 2012-02-15 12:10:06
 */
public class BubbleRooms {
    static private RoomModel s_roommodel;
    static RoomModel roomModel(){
        if( s_roommodel==null ){
            String path = Config.getValue("BubblePath");
            int cachenumRoom = Integer.parseInt(Config.getValue("BubbleRoomCacheSize"));
            long lifetime = Long.parseLong(Config.getValue("BubbleLifeTime"));
            s_roommodel = new RoomModel(path, cachenumRoom, lifetime);
        }
        return s_roommodel;
    }
}

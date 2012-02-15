/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.friends;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.cache.RoomModel;

/**
 *
 * @author yuy
 * @date 2012-02-15 12:10:06
 */
public class FriendRooms {
    static private RoomModel s_roommodel;
    static RoomModel roomModel(){
        if( s_roommodel==null ){
            String path = Config.getValue("FriendPath");
            int cachenumRoom = Integer.parseInt(Config.getValue("FriendRoomCacheSize"));
            long lifetime = Long.parseLong(Config.getValue("FriendLifeTime"));
            s_roommodel = new RoomModel(path, cachenumRoom, lifetime);
        }
        return s_roommodel;
    }
}

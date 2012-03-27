/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.info;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.cache.RoomModel;

/**
 *
 * @author yuy
 * @date 2012-02-20 12:39:54
 */
public class InfoRooms {
    static private RoomModel s_roommodel;
    static RoomModel roomModel(){
        if( s_roommodel==null ){
            String path = Config.getValue("InfoPath");
            int cachenumRoom = Integer.parseInt(Config.getValue("InfoRoomCacheSize"));
            long lifetime = Long.parseLong(Config.getValue("InfoLifeTime"));
            s_roommodel = new RoomModel(path, cachenumRoom, lifetime);
        }
        return s_roommodel;
    }
}

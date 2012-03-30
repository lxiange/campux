/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.classroom;

import com.bizdata.campux.server.Config;
import com.bizdata.campux.server.Log;
import com.bizdata.campux.server.cache.RoomModel;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author yuy
 * @date 2012-02-19 10:47:57
 */
public class ClassRooms {
    static private RoomModel s_roommodel;
    static private HashMap<String, String[]> s_prearrange = null;
    static private HashMap<String, String> s_dayswitch = null;
    static private long s_lastloadtime=0;
    
    static RoomModel roomModel(){
        if( s_roommodel==null ){
            String path = Config.getValue("ClassRoomPath");
            int cachenumRoom = Integer.parseInt(Config.getValue("ClassRoomCacheSize"));
            long lifetime = Long.parseLong(Config.getValue("ClassRoomLifeTime"));
            s_roommodel = new RoomModel(path, cachenumRoom, lifetime);
        }
        return s_roommodel;
    }
    
    static double[] s_timestarts = {8-0.167,10-0.167,14-0.167,16-0.167,18.5-0.167};
    static double[] s_timeends = {10-0.167,12-0.167,16-0.167,18-0.167,20.5-0.167};
    
    static boolean isPrearranged(String room){
        if( s_prearrange==null ){
            loadPrearrange();
        }
        
        System.out.println(room);
        String[] arr = s_prearrange.get(room);
        System.out.println(arr);
        if( arr==null )
            return false;
        
        // get current time
        Calendar rightNow = Calendar.getInstance();
        
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int month = rightNow.get(Calendar.MONTH)+1;
        dayswitch();
        String daystr = month + "." + day;
        String switchday = s_dayswitch.get(daystr);
        if( switchday == null ){
            // do nothing
        }else if( switchday.isEmpty() ){
            // holiday
            return false;
        }else{
            // switch
            int numswitch = Integer.parseInt(switchday);
            rightNow.add(Calendar.DAY_OF_YEAR, numswitch);
        }
        
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        day = rightNow.get(Calendar.DAY_OF_WEEK);
        double hours = (double)hour + (double)minute/60.0;
        int interval = -1;
        for(int i=0; i<5; i++){
            if( hours >= s_timestarts[i] && hours<=s_timeends[i]){
                interval = i;
                break;
            }
        }
        System.out.println("hours:"+hours);
        System.out.println("interval:"+interval);
        // not at the studying time
        if( interval==-1 )
            return false;
        
        day = day-2;
        if( day==-1 ) day=7;
        System.out.println("day:"+day);
        
        String token = arr[day*7+interval];
        System.out.println("token:"+token);
        if( "E".equalsIgnoreCase(token) )
            return false;
        else if( "B".equalsIgnoreCase(token) )
            return true;
        
        int weeks = rightNow.get(Calendar.WEEK_OF_YEAR) - 6;
        boolean sd = weeks%2==1;
        if( sd && "S".equalsIgnoreCase(token))
            return true;
        else if ( !sd && "D".equalsIgnoreCase(token))
            return true;
        
        return false;
    }
    
    static void loadPrearrange(){
        s_prearrange = new HashMap<String, String[]>();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("classroom_prearrange.txt"),Config.getCharset()));
            String line = null;
            while( (line=reader.readLine())!=null ){
                System.out.println(line);
                String seps[] = line.split(",");
                if( seps.length < 36 )
                    continue;
                String room = seps[0];
                String arrange[] = new String[35];
                System.arraycopy(seps, 1, arrange, 0, 35);
                s_prearrange.put(room, arrange);
            }
            reader.close();
        }catch(Exception exc){
            Log.log("ClassRoom", Log.Type.NOTICE, exc);
        }
    }
    
    static void dayswitch(){
        long time = System.currentTimeMillis();
        if( !(time - s_lastloadtime > 7200000)) // two hours
            return;
        s_lastloadtime = time;
        s_dayswitch = new HashMap<String, String>();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("day-switch.txt"),Config.getCharset()));
            String line = null;
            while( (line=reader.readLine())!=null ){
                System.out.println(line);
                String seps[] = line.split(",");
                String fromDay = seps[0].trim();
                String toDay = seps[1].trim();
                s_dayswitch.put(fromDay, toDay);
            }
            reader.close();
        }catch(Exception exc){
            Log.log("ClassRoom", Log.Type.NOTICE, exc);
        }
    }
    
    static public void main(String[] args){
        boolean b = isPrearranged("环境科学楼B_101");
        System.out.println(b);
    }
}

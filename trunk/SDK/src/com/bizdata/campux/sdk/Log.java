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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * this static class is used for logging information
 * @author yuy
 */
public class Log {
    /**
     * type of a log
     */
    public enum Type {INFO, NOTICE, ERROR, FATAL}
    /**
     * log an event
     * @param app name of an applications
     * @param type log type
     * @param msg message
     */
    public static void log(String app, Type type, String msg){
        String typestr = null;
        switch(type){
            case INFO: typestr = "INFO"; break;
            case NOTICE: typestr = "NOTICE"; break;
            case ERROR: typestr = "ERROR"; break;
            case FATAL: typestr = "FATAL"; break;
        }
        
        System.out.println(app+"::"+typestr+":"+msg);
    }
    /**
     * log an exception
     * @param app name of an application
     * @param type log type
     * @param exc an Exception
     */
    public static void log(String app, Type type, Exception exc){
        String typestr = null;
        switch(type){
            case INFO: typestr = "INFO"; break;
            case NOTICE: typestr = "NOTICE"; break;
            case ERROR: typestr = "ERROR"; break;
            case FATAL: typestr = "FATAL"; break;
        }
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        exc.printStackTrace( new PrintStream(buffer) );
        
        System.out.println(app+"::"+typestr+":"+buffer.toString());
    }
}

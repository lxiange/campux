/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 *
 * @author yuy
 */
public class Log {
    public enum Type {INFO, NOTICE, ERROR, FATAL}
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

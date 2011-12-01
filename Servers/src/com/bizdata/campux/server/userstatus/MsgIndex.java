/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.userstatus;

/**
 *
 * @author yuy
 * @date 2011-11-29 01:49:30
 */
public class MsgIndex implements java.io.Serializable{
    public String block;
    public long start;
    public int id;
    public MsgIndex clone(){
        MsgIndex newindex = new MsgIndex();
        newindex.block = block;
        newindex.start = start;
        newindex.id = id;
        return newindex;
    }
}

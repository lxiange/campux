/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.cache;

/**
 *
 * @author yuy
 * @date 2012-02-14 02:12:13
 */
public class Piece implements java.io.Serializable{
    public long m_timestamp;
    public String m_content;
    public boolean block = false;
}

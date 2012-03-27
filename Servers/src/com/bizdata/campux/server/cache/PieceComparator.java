/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.server.cache;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author yuy
 * @date 2012-03-12 11:44:59
 */
public class PieceComparator implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        Piece p1 =(Piece)o1;
        Piece p2=(Piece)o2;
        if( p1.m_timestamp>p2.m_timestamp)
            return -1;
        else if( p1.m_timestamp<p2.m_timestamp)
            return 1;
        else
            return 0;
    }
}

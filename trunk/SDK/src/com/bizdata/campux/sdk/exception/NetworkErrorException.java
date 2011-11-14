/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizdata.campux.sdk.exception;

/**
 *
 * @author yuy
 */
public class NetworkErrorException extends Exception{
    String m_message;
    public NetworkErrorException(Exception exc){
        this.m_message = exc.getMessage();
    }

    @Override
    public String getMessage() {
        return m_message;
    }    
}

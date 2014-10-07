/*
 * EMail.java
 *
 * Created on 12/08/2008
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.api;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class EMail 
{
    private String address;

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public EMail(String address)
    {
        this.address = address;
    }

    public Boolean isValid()
    {
        String emailRegEx = "^\\S+@\\S+$";
        
        return (address != null && address.matches(emailRegEx));
    }
    
} // prueba
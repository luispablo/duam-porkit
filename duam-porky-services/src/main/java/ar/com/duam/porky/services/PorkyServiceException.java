/*
 * PorkyServiceException.java
 *
 * Created on July, 2010
 *
 * Copyright(c) 2010 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.services;

public class PorkyServiceException extends Exception 
{
	private static final long serialVersionUID = -928661053236617334L;

	public PorkyServiceException (String message)
	{
		super(message);
	}
	
	public PorkyServiceException (String message, Throwable cause)
	{
		super(message, cause);
	}
}
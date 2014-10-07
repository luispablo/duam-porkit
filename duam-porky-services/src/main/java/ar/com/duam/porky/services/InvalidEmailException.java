/*
 * InvalidEmailException.java
 *
 * Created on July, 2010
 *
 * Copyright(c) 2010 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.services;

public class InvalidEmailException extends Exception 
{
	private static final long serialVersionUID = -928661053236617334L;

	public InvalidEmailException (String message)
	{
		super(message);
	}
	
	public InvalidEmailException (String message, Throwable cause)
	{
		super(message, cause);
	}
}
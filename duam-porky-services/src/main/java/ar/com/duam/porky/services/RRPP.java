/*
 * RRPP.java
 *
 * Created on 20/05/2009
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.services;

import java.util.Arrays;
import java.util.Collections;

import ar.com.duam.api.EMail;
import ar.com.duam.porky.model.Usuario;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class RRPP
{
    private Postman postman;

    public Postman getPostman()
    {
        return postman;
    }

    public void setPostman(Postman postman)
    {
        this.postman = postman;
    }

    @SuppressWarnings("unchecked")
	public void sendSuggest(Usuario usuario, String from, String to, String comments) throws Exception
    {
    	if (to != null)
    	{
        	// Parsear el to en direcciones de mail.
    		String[] tos = to.split(",");

        	// Validar las direcciones de mail, arrojar error si alguna inválida.
    		for (String email : tos)
    		{
    			EMail aux = new EMail(email);
    			
    			if (!aux.isValid())
    			{
    				throw new InvalidEmailException(email);
    			}
    		}
    		
        	// Armar el cuerpo del mail
    		Templater templater = new Templater();
    		String mailBody = templater.getSuggestMail(from, usuario.getEmail(), to, comments);
    		
        	// Hacer cada envío.
    		this.postman.send(Arrays.asList(tos), Collections.EMPTY_LIST, usuario.getNombre() +" te recomienda DUAM Porky.", mailBody);
    	}
    	else
    	{
    		throw new PorkyServiceException("Missing destination: Param 'to' is null.");
    	}
    }
}
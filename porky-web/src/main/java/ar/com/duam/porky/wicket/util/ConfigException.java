package ar.com.duam.porky.wicket.util;

import java.lang.RuntimeException;

public class ConfigException extends RuntimeException
{
	private static final long serialVersionUID = 2311953808438815911L;

    public ConfigException(String message) 
    {
        super(message);
    }
    
}

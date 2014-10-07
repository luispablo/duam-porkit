/*
 * Postman.java
 *
 * Created on July, 2010
 *
 * Copyright(c) 2010 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.services;

import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class Postman
{

	private static final Logger logger = Logger.getLogger(Postman.class);
    private String host;
    private String userName;
    private String password;
    private String fromAddress;
    private String fromName;
    private Integer smtpPort;
    private Boolean tls = false;

    public Postman()
    {
    	// do nothing.
    }
    
    public void send(List<String> tos, List<String> bccs, String subject, String body) throws Exception
    {
        HtmlEmail mail = new HtmlEmail();

        if (userName != null && password != null && userName.trim().length() > 0 && password.trim().length() > 0)
        {
        	mail.setAuthenticator(new DefaultAuthenticator(this.userName, this.password));
        }

        mail.setHostName(host);
        mail.setFrom(fromAddress, fromName);
        
        if (this.smtpPort != null)
        {
        	mail.setSmtpPort(this.smtpPort);
        }

    	mail.setSSL(this.tls);        	
        
        for (String to : tos)
        {
        	logger.debug(" To: "+ to);
            mail.addTo(to);
        }

        if (bccs != null)
        {
            for (String bcc : bccs)
            {
                mail.addBcc(bcc);
            }
        }

        mail.setSubject(subject);
        mail.setHtmlMsg(body);

        mail.send();
    }

    public String getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress)
    {
        this.fromAddress = fromAddress;
    }

    public String getFromName()
    {
        return fromName;
    }

    public void setFromName(String fromName)
    {
        this.fromName = fromName;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

	public Integer getSmtpPort() 
	{
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) 
	{
		this.smtpPort = smtpPort;
	}

	public Boolean getTls() 
	{
		return tls;
	}

	public void setTls(Boolean tls) 
	{
		this.tls = tls;
	}
    
}
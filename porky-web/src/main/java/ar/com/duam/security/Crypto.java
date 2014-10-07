/*
 * Crypto.java
 *
 * Created on 20 de noviembre de 2006, 23:24
 *
 * Copyright(c) 2006 FIEL (Fundaci�n de Investigaciones Econ�micas Latinoamericanas).
 * All Rights Reserved.
 * This software is the proprietary information of FIEL.
 */
package ar.com.duam.security;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;

/**
 * @author Ing. Luis Pablo Gallo (luispablo.gallo@gmail.com)
 */
public class Crypto
{
 
    public static String encrypt(String original)
    throws Exception
    {
        MessageDigest d = MessageDigest.getInstance(SecurityConstants.ENCRYPTION_ALGORITHM);
        d.reset();
        d.update(original.getBytes());

        Base64 base = new Base64();
        return new String(base.encode(d.digest()));
    }
    
}
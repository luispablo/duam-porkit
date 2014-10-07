/*
 * ServicesConstants.java
 *
 * Created on 14 de noviembre de 2006, 19:37
 *
 * Copyright(c) 2006 FIEL (Fundaci�n de Investigaciones Econ�micas Latinoamericanas).
 * All Rights Reserved.
 * This software is the proprietary information of FIEL.
 */
package ar.com.duam.security;

/**
 * @author Ing. Luis Pablo Gallo (luispablo.gallo@gmail.com)
 */
public class SecurityConstants
{
    // DES (Data Encryption Standard) cipher in Electronic Codebook mode, with 
    // PKCS #5-style padding.
    public static final String DES = "DES";
    public static final String DES_ECB_PKCS5Padding = "DES/ECB/PKCS5Padding";
    
    public static final String PBE_MD5_DES = "PBEWithMD5AndDES";    
    
    public static final String ENCRYPTION_ALGORITHM = "SHA-1";
    public static final String UTF8 = "UTF8";
    public static final String DEFAULT_PASSPHRASE = "defpassph";
    
}
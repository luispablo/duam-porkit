/*
 * DataProvider.java
 *
 * Created on {date}
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.dao;

import java.util.List;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public interface DataProvider
{

    @SuppressWarnings("rawtypes")
	public List find(Class clazz, List<FilterItem> filterItems);

}
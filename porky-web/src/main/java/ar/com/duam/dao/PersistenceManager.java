/*
 * PersistenceManager.java
 *
 * Created on 05/03/2009
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.dao;

import java.util.List;

import com.google.inject.persist.Transactional;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
@Transactional
public interface PersistenceManager
{

    public <T> List<T> findAll(Class<?> entityClass);

    public <T> T find(Class<?> entityClass, Long id);

    @Transactional
    public void save(Object entity);

    @Transactional
    public <T> T merge(T entity);

    @Transactional
    public void remove(Object entity);
}
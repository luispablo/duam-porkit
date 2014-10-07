/*
 * FilterItem.java
 *
 * Created on 07/03/2009
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.dao;

import java.io.Serializable;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class FilterItem implements Serializable
{

    private static final long serialVersionUID = -432897311283170638L;

    public enum Comparator
    {

        EQUAL,
        NOT_EQUAL,
        GREATER,
        GREATER_EQUAL,
        LOWER,
        LOWER_EQUAL,
        LIKE
    }
    private String[] properties;
    @SuppressWarnings("rawtypes")
	private Class type;
    private Comparator comparator;
    private Object[] possibleValues;
    private Object value;
    protected Boolean visible = true;

    public Boolean isVisible()
    {
        return visible;
    }

    public void setVisible(Boolean visible)
    {
        this.visible = visible;
    }

    @SuppressWarnings("rawtypes")
	public FilterItem(String property, Class type, Comparator comparator, Object defaultValue)
    {
        this(property, type, comparator);

        this.value = defaultValue;
    }

    @SuppressWarnings("rawtypes")
	public FilterItem(String property, Class type, Comparator comparator, Object[] possibleValues, Object defaultValue)
    {
        this(property, type, comparator);

        this.possibleValues = possibleValues;
        this.value = defaultValue;
    }

    @SuppressWarnings("rawtypes")
	public FilterItem(String property, Class type, Comparator comparator, Object[] possibleValues)
    {
        this(property, type, comparator);

        this.possibleValues = possibleValues;
    }

    @SuppressWarnings("rawtypes")
	public FilterItem(String property, Class type, Comparator comparator)
    {
        this(new String[]
                {
                    property
                }, type, comparator);
    }

    @SuppressWarnings("rawtypes")
	public FilterItem(String[] properties, Class type, Comparator comparator)
    {
        this.properties = properties;
        this.type = type;
        this.comparator = comparator;
    }

    public String getComparatorSymbol()
    {
        switch (comparator)
        {
            case EQUAL:
                return "=";
            case NOT_EQUAL:
                return "<>";
            case GREATER:
                return ">";
            case GREATER_EQUAL:
                return ">=";
            case LOWER:
                return "<";
            case LOWER_EQUAL:
                return "<=";
            case LIKE:
                return "like";
        }

        return "#invalid#";
    }

    public Boolean isEmpty()
    {
        if (value != null)
        {
            if (value instanceof String)
            {
                return ((String) value).trim().length() == 0;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public Comparator getComparator()
    {
        return comparator;
    }

    public void setComparator(Comparator comparator)
    {
        this.comparator = comparator;
    }

    public String[] getProperties()
    {
        return properties;
    }

    public void setProperties(String[] properties)
    {
        this.properties = properties;
    }

    @SuppressWarnings("rawtypes")
	public Class getType()
    {
        return type;
    }

    @SuppressWarnings("rawtypes")
	public void setType(Class type)
    {
        this.type = type;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public Object[] getPossibleValues()
    {
        return possibleValues;
    }

    public void setPossibleValues(Object[] possibleValues)
    {
        this.possibleValues = possibleValues;
    }
}
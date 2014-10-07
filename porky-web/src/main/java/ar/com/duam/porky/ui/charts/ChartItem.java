/*
 * ChartItem.java
 *
 * Created on 30/09/2008
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.charts;

import java.io.Serializable;

/**
 * <p>Atomic unit of data from a chart.</p>
 * <br>
 * 
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class ChartItem implements Serializable
{
	private static final long serialVersionUID = -5042762050122118860L;
	
	private String label;
    private Double value;

    public ChartItem(String label, Double value)
    {
        this.label = label;
        this.value = value;
    }
    
    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }
    
    public void onClick()
    {        
    }
    
}
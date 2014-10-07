/*
 * Chart.java
 *
 * Created on 30/09/2008
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.charts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.EntityCollection;

/**
 * <p>Abstract class to create a JFreeChart based chart.</p>
 * <br>
 * 
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public abstract class Chart 
{
    protected JFreeChart chart;
    protected List<ChartItem> items;    
    
    public JFreeChart getChart()
    {
        return chart;
    }

    /**
     * <p>Get the entity collection (items to be displayed in the chart) within
     * an image of the given width an height.</p>
     * <br>
     * 
     * @param width
     * @param height
     * @return EntityCollection
     */
    public EntityCollection getEntityCollection(Integer width, Integer height)
    {
        // Somewhere to virtually draw the chart, as to calculate positions.
        BufferedImage dummy = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);        
        Rectangle rectangle = new Rectangle(width, height);

        // Draw the chart, as to calculate positions.
        ChartRenderingInfo info = new ChartRenderingInfo();
        chart.draw((Graphics2D)dummy.getGraphics(), rectangle, info);
        
        return info.getEntityCollection();
    }

    public List<ChartItem> getItems()
    {
        return items;
    }
        
}
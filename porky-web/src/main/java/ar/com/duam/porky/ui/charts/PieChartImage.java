/*
 * PieChartImage.java
 *
 * Created on 30/09/2008
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.charts;

import java.util.List;

/**
 * <p>This is an utility class to avoid building both the chart and the image
 * afterwards.</p>
 * <p>Only using this class you can build an image with a JFreeChart inside.</p>
 * <br>
 * 
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PieChartImage extends ChartImage
{
	private static final long serialVersionUID = -1121933470984391714L;
   
    /**
     * <p>Builds the image, using the given items for the pie chart JFreeChart
     * inside it.</p>
     * <br>
     * 
     * @param id Wicket ID.
     * @param items Items to build the pie chart.
     * @param width Total image width.
     * @param height Total image height.
     */
    public PieChartImage(String id, List<ChartItem> items, Integer width, Integer height)
    {
        super(id);
        
        this.width = width;
        this.height = height;
        
        chart = new PieChart(items);
        
        buildResource();
    }
    
}
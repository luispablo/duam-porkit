/*
 * ChartImage.java
 *
 * Created on 30/09/2008
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.charts;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;

/**
 * <p>This class builds a Wicket image, based on a DUAM chart object.</p>
 * <br>
 * 
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class ChartImage extends Image
{
	private static final long serialVersionUID = -4326391726464185736L;
	
	protected Chart chart;
    protected Integer width;
    protected Integer height;

    /**
     * <p>Only builds an instance. Private use for its children classes.</p>
     * <br>
     * 
     * @param id Wicket ID.
     */
    protected ChartImage(String id)
    {
        super(id);
    }
    
    /**
     * <p>Builds the image with the given Wicket ID, filling the image with the
     * provided chart.</p>
     * <br>
     * 
     * @param id Wicket ID.
     * @param chart Chart to be displayed.
     * @param width Total image width.
     * @param height Total image height.
     */
    public ChartImage(String id, final Chart chart, final Integer width, final Integer height)
    {
        super(id);
        
        this.chart = chart;
        this.width = width;
        this.height = height;
        
        // This method links Wicket and JFreeChart.
        buildResource();
    }
    
    /**
     * <p>Here is built the Wicket resource to put the JFreeChart chart into the
     * Wicket image.</p>
     * <br>
     */
    protected void buildResource()
    {
        // Builds resource with given parameters.
        RenderedDynamicImageResource resource = new RenderedDynamicImageResource(width, height)
        {
			private static final long serialVersionUID = 3369026095324484753L;

			@Override
			protected boolean render(Graphics2D graphics, Attributes attributes) 
			{
                // Do the magic!
                Rectangle rectangle = new Rectangle(width, height);
                chart.getChart().draw(graphics, rectangle);
                chart.getChart().setBackgroundPaint(new Color(237, 237, 237));
                
                return true;
			}
        };

        setImageResource(resource);        
    }
    
}
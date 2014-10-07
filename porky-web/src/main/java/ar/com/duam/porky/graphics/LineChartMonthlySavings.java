/*
 * LineChartConceptosMensual.java
 *
 * Created on 10/01/2009
 *
 * Copyright(c) 2009 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class LineChartMonthlySavings implements Serializable
{
    private static final long serialVersionUID = -2402665276233538991L;

    private static final Logger logger = Logger.getLogger(LineChartMonthlySavings.class);
    
    private Map<Date, Float> values;
	private String chartTitle = "";
	private String xTitle = "";
	private String yTitle = "";
    private static Integer width = 600;
    private static Integer height = 80;
    private Color backgroundColor = Color.WHITE;
    private Float max = 0f;
    private Float min = 9999999f;
    
    public LineChartMonthlySavings(Map<Date, Float> values)
    {
		this.values = values;
    }

    public IResource getImageResource()
    {
		CategoryDataset dataset = this.createDataset(values);
		JFreeChart chart = createChart(dataset, chartTitle, xTitle, yTitle);
		
		return createResource(chart);    	
    }
    
    public Image getImage(String id)
    {
        return new Image(id, getImageResource());
    }

    /**
     * <p>Arma el Resource que le
     * debe dar al objeto Image para renderizarse.</p>
     * <br>
     * 
     * @param totales Datos para armar el gráfico.
     * @return Resource
     */
    public IResource createResource(final JFreeChart chart)
    {
        return new RenderedDynamicImageResource(width, height)
        {
			private static final long serialVersionUID = -5106178766184360917L;

			protected boolean render(Graphics2D graphics, Attributes attrs)
            {
				drawChart(chart, graphics);

                return true;
            }
        };
    }

	/**
	 * <p>Build the dataset based on a collection of monthly concept
	 * totals.</p>
	 * <br/>
	 **/
	public CategoryDataset createDataset(Map<Date, Float> values)
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yy");

        List<Date> dates = new ArrayList<Date>(values.keySet());
        Collections.sort(dates);
        
        if (!dates.isEmpty())
        {
    		for (Date month : dates)
    		{
    			Float saving = values.get(month);

    			if (saving > this.max)
    			{
    				this.max = saving;
    			}
    			if (saving < this.min)
    			{
    				this.min = saving;
    			}
    			
    			logger.debug("Saving = "+ saving);
    			
    			dataset.addValue(saving, "Saldo", sdf.format(month));
    		}
        }
        else
        {
        	this.max = null;
        	this.min = null;
        }

		return dataset;
	}

	/**
	 * <p>Create the 2D line chart with the given
	 * information.</p>
	 * <br/>
	 **/
	public JFreeChart createChart	( CategoryDataset dataset
									, String chartTitle 
									, String xTitle
									, String yTitle )
	{
		JFreeChart chart = ChartFactory.createLineChart	( chartTitle
														, xTitle
														, yTitle
														, dataset
														, PlotOrientation.VERTICAL // orientation
														, false // include legend
														, true // tooltips
														, true ); // URLs

        chart.setBackgroundPaint(this.backgroundColor);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeCrosshairVisible(true);

        if (this.min != null && this.max != null)
        {
            // 10% of margin
            double aux = (this.max - this.min) * 0.3;
            plot.getRangeAxis().setRange(new Range(this.min - aux, this.max + aux), true, true);
        }
        
        LineAndShapeRenderer lrenderer = (LineAndShapeRenderer) plot.getRenderer();
        lrenderer.setSeriesStroke(0, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,1.0f, new float[] {6.0f, 6.0f}, 0.0f));
        lrenderer.setSeriesShapesVisible(0, true);
        lrenderer.setDrawOutlines(true);
        lrenderer.setUseFillPaint(true);
        lrenderer.setSeriesFillPaint(0, Color.yellow);
        
        CategoryItemRenderer cir = (CategoryItemRenderer) plot.getRenderer();
        cir.setSeriesItemLabelGenerator(0, new StandardCategoryItemLabelGenerator());
        cir.setSeriesItemLabelsVisible(0, true);
        
		return chart;
	}

	/**
	 * <p>Draws the given chart in the given graphics.</p>
	 * <br/>
	 **/
	public void drawChart(JFreeChart chart, Graphics2D graphics)
	{
		Rectangle rectangle = new Rectangle(width, height);
		chart.draw(graphics, rectangle);
	}

	public void setChartTitle(String value)
	{
		chartTitle = value;
	}
	public String getChartTitle()
	{
		return chartTitle;
	}
	
	public void setXTitle(String value)
	{
		xTitle = value;
	}
	public String getXTitle()
	{
		return xTitle;
	}
	
	public void setYTitle(String value)
	{
		yTitle = value;
	}
	public String getYTitle()
	{
		return yTitle;
	}
	
	public void setWidth(Integer value)
	{
		width = value;
	}
	public Integer getWidth()
	{
		return width;
	}
	
	public void setHeight(Integer value)
	{
		height = value;
	}
	public Integer getHeight()
	{
		return height;
	}

	public void setBackgroundColor(Color backgroundColor) 
	{
		this.backgroundColor = backgroundColor;
	}

	public Color getBackgroundColor() 
	{
		return backgroundColor;
	}
	
}
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import ar.com.duam.porky.model.Concepto;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class LineChartConceptosMensual implements Serializable
{
    private static final long serialVersionUID = -2402665276233538991L;
    
	private Map<Concepto, Map<Date, Double>> values;
	private String chartTitle = "";
	private String xTitle = "";
	private String yTitle = "";
    private static Integer width = 500;
    private static Integer height = 350;

    public LineChartConceptosMensual(Map<Concepto, Map<Date, Double>> values)
    {
		this.values = values;
    }

    public Image getImage(String id)
    {
		CategoryDataset dataset = createDataset(values);
		JFreeChart chart = createChart(dataset, chartTitle, xTitle, yTitle);
		chart.setBackgroundPaint(new Color(237, 237, 237));
		chart.getPlot().setBackgroundPaint(Color.WHITE);
        return new Image(id, createResource(chart));
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
			private static final long serialVersionUID = 1L;

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
	public CategoryDataset createDataset(Map<Concepto, Map<Date, Double>> values)
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");

		for (Concepto concepto : values.keySet())
		{
            Map<Date, Double> totals = values.get(concepto);
            List<Date> months = new ArrayList<Date>(values.get(concepto).keySet());
            Collections.sort(months);
			
			for (Date month : months)
			{
				dataset.addValue(totals.get(month), concepto.getNombre(), sdf.format(month));
			}
		}
		
		return dataset;
	}

	/**
	 * <p>Create the 2D line chart with the given
	 * information.</p>
	 * <br/>
	 **/
	@SuppressWarnings({ "deprecation" })
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
														, true // include legend
														, true // tooltips
														, true ); // URLs

        chart.setBackgroundPaint(Color.white);

        CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) chart.getCategoryPlot().getRenderer();
        //Enable shapes on the line chart
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseSeriesVisible(true);
        renderer.setShapesVisible(true);
        renderer.setDrawOutlines(true);

        DecimalFormat decimalformat1 = new DecimalFormat("#0.00");
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));

        //Create Dotted or Dashed lines
        renderer.setBaseStroke(new BasicStroke  ( 2.0f
                                                , BasicStroke.CAP_BUTT
                                                , BasicStroke.JOIN_MITER
                                                , 1.0f
                                                , new float[] {10.0f, 6.0f}
                                                , 0.0f ));

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
	
}
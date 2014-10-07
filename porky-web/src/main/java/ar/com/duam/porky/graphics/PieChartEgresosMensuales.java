/*
 * PieChartEgresosMensuales.java
 *
 * Created on 10/01/2008
 *
 * Copyright(c) 2007 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import ar.com.duam.porky.model.Concepto;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PieChartEgresosMensuales implements Serializable
{

    private static final long serialVersionUID = 4441790474721471172L;
    private IResource resource;
    private Integer width = 500;
    private Integer height = 350;
    private Color backgroundColor = Color.WHITE;

    public PieChartEgresosMensuales(List<Object[]> totales)
    {
        this.resource = this.getGraficoResource(totales);
    }

    public Image getImage(String id)
    {
        return new Image(id, this.resource);
    }

    /**
     * <p>Pide el gráfico y, con el mismo, arma el Resource que le
     * debe dar al objeto Image para renderizarse.</p>
     * <br>
     * 
     * @param totales Datos para armar el gráfico.
     * @return Resource
     */
    public IResource getGraficoResource(final List<Object[]> totales)
    {
        return new RenderedDynamicImageResource(PieChartEgresosMensuales.this.width, PieChartEgresosMensuales.this.height)
        {
			private static final long serialVersionUID = 5130370241440645481L;

			protected boolean render(Graphics2D graphics, Attributes attrs)
            {
                JFreeChart chart = PieChartEgresosMensuales.this.createChart(totales);
                Rectangle rectangle = new Rectangle(PieChartEgresosMensuales.this.width, PieChartEgresosMensuales.this.height);

                chart.draw(graphics, rectangle);

                return true;
            }
        };
    }

    /**
     * <p>Este método arma el gráfico de torta en base al dataset
     * proporcionado.</p>
     * <br>
     * 
     * @param dataset Datos para el gráfico
     * @return JFreeChart
     */
    private JFreeChart createChart(List<Object[]> totales)
    {
        DefaultPieDataset dataset = this.createDataset(totales);
        JFreeChart chart = ChartFactory.createPieChart3D(null, dataset, true, true, true);

        final DecimalFormat df = new DecimalFormat("#,###,##0");
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(this.backgroundColor);
        plot.setOutlinePaint(null);
        plot.setMaximumLabelWidth(0.2);
        plot.setLabelGenerator(new PieSectionLabelGenerator() 
        {
			@SuppressWarnings("rawtypes")
			public String generateSectionLabel(PieDataset dataset, Comparable key) 
			{
				return key.toString();
			}
			
			@SuppressWarnings("rawtypes")
			public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) 
			{
				return null;
			}
		});
        plot.setLegendLabelGenerator(new PieSectionLabelGenerator() 
        {
			@SuppressWarnings("rawtypes")
			public String generateSectionLabel(PieDataset dataset, Comparable key) 
			{
				return df.format((Double) dataset.getValue(key));
			}
			
			@SuppressWarnings("rawtypes")
			public AttributedString generateAttributedSectionLabel(PieDataset arg0,	Comparable arg1) 
			{
				return null;
			}
		});
        
        chart.setBackgroundPaint(this.backgroundColor);

        return chart;
    }

    /**
     * <p>Este método arma el dataset para este tipo de gráfico en base a una
     * colección de pares Concepto - Total.</p>
     * <br>
     * 
     * @param totales Colección de pares Concepto - Total.
     * @return DefaultPieDataset
     */
    private DefaultPieDataset createDataset(List<Object[]> totales)
    {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Object[] row : totales)
        {
            Concepto concepto = (Concepto) row[0];
            Double total = (Double) row[1];
            dataset.setValue(concepto.getNombreTruncated(), total);
        }

        return dataset;
    }

	public Integer getWidth() 
	{
		return width;
	}

	public void setWidth(Integer width) 
	{
		this.width = width;
	}

	public Integer getHeight() 
	{
		return height;
	}

	public void setHeight(Integer height) 
	{
		this.height = height;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

}
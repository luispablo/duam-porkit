/*
 * PieChart.java
 *
 * Created on 30/09/2008
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.charts;

import java.awt.Color;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 * <p>Pie chart based on JFreeChart PieChart.</p>
 * <br>
 * 
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PieChart extends Chart implements Serializable
{
	private static final long serialVersionUID = -8358648096687784139L;

	/**
     * <p>Builds the chart only with the data items.</p>
     * <br>
     * 
     * @param items Data to be displayed in the pie.
     */
    public PieChart(List<ChartItem> items)
    {
        this(items, "#0.00#");
    }
    
    /**
     * <p>Builds the data with the given items, formatting the decimal values
     * with the provided format.</p>
     * <br>
     * 
     * @param items Data to be displayed in the pie.
     * @param decimalFormat Format for the decimal values (in the labels).
     */
    public PieChart(List<ChartItem> items, String decimalFormat)
    {
        // Save this for future reference.
        this.items = items;
        
        // Builds the JFreeChart dataset.
        DefaultPieDataset dataset = new DefaultPieDataset();
        DecimalFormat format = new DecimalFormat(decimalFormat);

        for (ChartItem item : items)
        {
            Double value = item.getValue();
            String formatedValue = format.format(value);
            dataset.setValue(item.getLabel() + " (" + formatedValue + ")", value);
        }

        // Builds the PieChart.
        chart = ChartFactory.createPieChart3D(null, dataset, false, false, false);

        // Some configuration.
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(237, 237, 237));
        plot.setOutlinePaint(null);
        
        chart.clearSubtitles();
        chart.setBackgroundPaint(new Color(237, 237, 237));
        chart.getPlot().setOutlinePaint(null);
    }

}
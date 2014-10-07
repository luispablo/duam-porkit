package ar.com.duam.porky.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author Luis Pablo Gallo (luispablo.gallo@gmail.com)
 */
public class IncomeOutcomeBarGraph implements Serializable
{
	private static final Logger logger = Logger.getLogger(IncomeOutcomeBarGraph.class);

	private static final long serialVersionUID = 4700243770312323652L;
    private static final String CATEGORY_NAME = "totales";
    private static final String GRAPHIC_TITLE = "Ingresos / Egresos";
    private String wicketId = null;
    private Double[] totales;
    private int width = 170; // Valor por defecto.
    private Boolean includeLegend = true;
    private Boolean showTitle = true;
    private Boolean rangeAxisVisible = true;
    private Color backgroundPaint = Color.WHITE;
    private Color chartBackgroundColor = new Color(237, 237, 237);

    private int height = 200; // Valor por defecto.

    private String fontFamilyName = "Arial";

    /**
     * Crea una nueva instancia de IncomeOutcomeBarGraph.
     *
     * @param container La página en la que se colocará.
     * @param wicketId ID del <img> de wicket en el HTML.
     * @param income Valor del total de ingresos.
     * @param outcome Valor del total de egresos.
     */
	public IncomeOutcomeBarGraph(String wicketId, Double[] totales)
    {
        this.wicketId = wicketId;
        this.setTotales(totales);
    }

    /**
     * Devuelve una imagen generada a partir de los parámetros indicados.
     *
     * @return Image
     */
    public Image getImage()
    {
        return new Image(wicketId, getGraficoResource());
    }
    
    public EntityCollection getEntityCollection()
    {
        BufferedImage dummy = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        JFreeChart chart = getChart();
        Rectangle rectangle = new Rectangle(width, height);

        ChartRenderingInfo info = new ChartRenderingInfo();
        chart.draw((Graphics2D)dummy.getGraphics(), rectangle, info);
        
        return info.getEntityCollection();
    }

    public IResource getGraficoResource()
    {
        RenderedDynamicImageResource resource = new RenderedDynamicImageResource(IncomeOutcomeBarGraph.this.width, IncomeOutcomeBarGraph.this.height)
        {
			private static final long serialVersionUID = -7112135909184199200L;

			protected boolean render(Graphics2D graphics, Attributes attrs)
            {
                JFreeChart chart = IncomeOutcomeBarGraph.this.getChart();
                Rectangle rectangle = new Rectangle(IncomeOutcomeBarGraph.this.width, IncomeOutcomeBarGraph.this.height);

                chart.draw(graphics, rectangle);
                return true;
            }
        };

        return resource;
    }

    /**
     * Devuelve el gráfico.
     *
     * @return JFreeChart
     */
    private JFreeChart getChart()
    {
        CategoryDataset dataset = this.createDataset(this.getTotales());
        JFreeChart chart = this.createChart(dataset);

        return chart;
    }

    /**
     * Crea el dataset con los valores dados, poniéndolos con formato de
     * currency como leyenda.
     *
     * @param totales Los totales del mes de cada concepto
     * @return CategoryDataset
     */
    private CategoryDataset createDataset(Double[] totales)
    {
        DecimalFormat df = new DecimalFormat("###,##0");

        String category1 = IncomeOutcomeBarGraph.CATEGORY_NAME;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Double total : totales)
        {
            if (total != null)
            {
            	logger.debug("Adding "+ total);
                dataset.addValue(total, df.format(total), category1);
            }
            else
            {
                dataset.addValue(0, df.format(0), category1);
            }
        }

        return dataset;
    }

    /**
     * Crea el gráfico y customiza sus propiedades.
     *
     * @param dataset Datos a volcar en el gráfico.
     * @return JFreeChart
     */
    private JFreeChart createChart(CategoryDataset dataset)
    {
        JFreeChart chart = ChartFactory.createBarChart("" // chart title
                , "Category" // domain axis label
                , "Value" // range axis label
                , dataset // data
                , PlotOrientation.VERTICAL // orientation
                , this.includeLegend // include legend
                , true // tooltips?
                , false);// URLs?

        if (this.showTitle)
        {
            TextTitle title = new TextTitle();
            title.setFont(new Font(this.fontFamilyName, Font.PLAIN, 16));
            title.setText(IncomeOutcomeBarGraph.GRAPHIC_TITLE);
            chart.setTitle(title);
        }
        
        chart.setBackgroundPaint(this.chartBackgroundColor);
        chart.getCategoryPlot().getDomainAxis().setVisible(false);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(this.backgroundPaint);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLabel(null);
        rangeAxis.setVisible(this.rangeAxisVisible);

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, new Color(0, 150, 0),
                0.0f, 0.0f, new Color(0, 255, 0));
        GradientPaint gp1 = new GradientPaint(
                0.0f, 0.0f, new Color(255, 0, 0),
                0.0f, 0.0f, new Color(150, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesItemLabelsVisible(0, true);
        renderer.setSeriesItemLabelsVisible(1, true);

        return chart;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public String getFontFamilyName()
    {
        return fontFamilyName;
    }

    public void setFontFamilyName(String fontFamilyName)
    {
        this.fontFamilyName = fontFamilyName;
    }

    public Double[] getTotales()
    {
        return totales;
    }

    public void setTotales(Double[] totales)
    {
        this.totales = totales;
    }

	public Boolean getIncludeLegend() 
	{
		return includeLegend;
	}

	public void setIncludeLegend(Boolean includeLegend) 
	{
		this.includeLegend = includeLegend;
	}

	public Boolean getShowTitle() 
	{
		return showTitle;
	}

	public void setShowTitle(Boolean showTitle) 
	{
		this.showTitle = showTitle;
	}

	public Color getBackgroundPaint() 
	{
		return backgroundPaint;
	}

	public void setBackgroundPaint(Color backgroundPaint) 
	{
		this.backgroundPaint = backgroundPaint;
	}

	public Boolean getRangeAxisVisible() 
	{
		return rangeAxisVisible;
	}

	public void setRangeAxisVisible(Boolean rangeAxisVisible) 
	{
		this.rangeAxisVisible = rangeAxisVisible;
	}

	public void setChartBackgroundColor(Color chartBackgroundColor) {
		this.chartBackgroundColor = chartBackgroundColor;
	}

	public Color getChartBackgroundColor() {
		return chartBackgroundColor;
	}
	
}
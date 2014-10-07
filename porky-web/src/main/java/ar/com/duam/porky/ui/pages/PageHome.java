/*
 * PageHome.java
 *
 * Copyright(c) 2010 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.pages;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.IResource;

import ar.com.duam.api.date.FirstDayOfMonth;
import ar.com.duam.api.date.LastDayOfMonth;
import ar.com.duam.components.ui.DropDownChoiceMonths;
import ar.com.duam.components.ui.DropDownChoiceYears;
import ar.com.duam.porky.graphics.IncomeOutcomeBarGraph;
import ar.com.duam.porky.graphics.LineChartMonthlySavings;
import ar.com.duam.porky.graphics.PieChartEgresosMensuales;
import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Movimiento;
import ar.com.duam.porky.model.Preferencia.Name;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.SessionPorky;
import ar.com.duam.porky.ui.charts.ChartItem;

import com.google.inject.Provider;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PageHome extends PagePorkyBase 
{
	private static final long serialVersionUID = 8953783168741373108L;

	private static final Logger logger = Logger.getLogger(PageHome.class);
	
	private DropDownChoiceMonths month;
	private DropDownChoiceYears year;
	private TextField<Float> saldo;
	private String icono;
	private Double income = 0d;
	private Double outcome = 0d;
	private Integer top = 0;

	@Inject
	private Provider<IService> svcProvider;
	
	private Color bgColor = new Color(237, 237, 237);
	
	@SuppressWarnings("rawtypes")
	public PageHome()
	{
        // Re-attach user to session to retrieve preferences.
		Usuario usuario = this.getUsuario();
		
		if (usuario != null)
		{
			usuario = svcProvider.get().merge(this.getUsuario());
			((SessionPorky) getSession()).setUsuario(usuario);
			this.top = usuario.getPrefIntValue(Name.INCOME_TOP_QUANTITY, "5");
		}
		
		Form form = new Form("form");
		
		Locale locale = this.getLocale();
		
		final WebMarkupContainer params = new WebMarkupContainer("params");
		params.setOutputMarkupId(true);
		params.add(month = new DropDownChoiceMonths("month", locale));
		params.add(year = new DropDownChoiceYears("year", locale));	

		try 
		{
			params.add(this.saldo = new TextField<Float>("saldo", new PropertyModel<Float>(this, "saldo")));
			params.add(new TextField<Integer>("top", new PropertyModel<Integer>(this, "top")));
			params.add(new TextField<Integer>("evolutionMonths", new PropertyModel<Integer>(this, "evolutionMonths")));
		} 
		catch (Exception exception) 
		{
			e(exception);
		}
		
		form.add(params);
		
		final WebMarkupContainer incomeOutcome = this.buildIncomeOutcome();
		incomeOutcome.setOutputMarkupId(true);
		add(incomeOutcome);
		
		final WebMarkupContainer incomeTops = this.buildIncomeTops();
		incomeTops.setOutputMarkupId(true);
		add(incomeTops);
		
		final WebMarkupContainer outcomeTops = this.buildOutcomeTops();
		outcomeTops.setOutputMarkupId(true);
		add(outcomeTops);
		
		final WebMarkupContainer evolution = this.buildEvolution();
		evolution.setOutputMarkupId(true);
		add(evolution);

		Button add = new Button("add")
		{
			private static final long serialVersionUID = -8517102770893922026L;

			@Override
			public void onSubmit() 
			{
                ar.com.duam.porky.model.Movimiento mov = new ar.com.duam.porky.model.Movimiento();
                Comunidad comunidad = getUsuario().getComunidad();
                mov.setComunidad((ar.com.duam.porky.model.Comunidad) svcProvider.get().find(ar.com.duam.porky.model.Comunidad.class, new Long(comunidad.getId())));
                mov.setFecha(new Date());

                setResponsePage(new PageMovimientoForm(mov));
			}			
		};
		add.setOutputMarkupId(false);
		params.add(add);
		
		AjaxButton update = new AjaxButton("update") 
		{
			private static final long serialVersionUID = -2934784038618014574L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) 
			{
				// Update user preferences.
				Usuario aux = (Usuario) svcProvider.get().merge(PageHome.this.getUsuario());
				aux.setPreferencia(Name.INCOME_TOP_QUANTITY, PageHome.this.top);
				((SessionPorky) this.getSession()).setUsuario(aux);
				
				// Update dates and totals.
				PageHome.this.update();
				
				// Update components
				target.add(incomeOutcome);
				target.add(incomeTops);
				target.add(outcomeTops);
				target.add(evolution);
				target.add(params);
			}
		};
		update.setOutputMarkupId(false);
		params.add(update);
		
		this.add(form);

		this.update();
	}

	private void update()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, Integer.parseInt(month.getValue()) - 1);
		calendar.set(Calendar.YEAR, Integer.parseInt(year.getValue()));
		
		Date from = new FirstDayOfMonth(calendar);
		Date to = new LastDayOfMonth(calendar);
		
		Usuario usuario = this.getUsuario();
		
		if (usuario != null)
		{
			Comunidad comunidad = this.getUsuario().getComunidad();
			
	        income = svcProvider.get().getTotalPorFactor(from, to, comunidad, 1);
	        outcome = svcProvider.get().getTotalPorFactor(from, to, comunidad, -1);
	        
	        Date hoy = new Date();
	        
        	Double ingresos = svcProvider.get().calcularIngresoTotal(comunidad, from);
        	logger.debug("Ingresos del mes: "+ ingresos);
        	Double egresos = svcProvider.get().calcularEgresoTotal(comunidad, from);
        	logger.debug("Egresos del mes: "+ egresos);
        	Double ahorroProg = comunidad.getAhorroProgramadoMes(from);
        	logger.debug("Ahorro programado: "+ ahorroProg);
        	Double ahorroReal = ingresos - egresos;
        	logger.debug("Ahorro real: "+ ahorroReal);
        	
        	String color = "";
        	
        	if (ahorroProg != null && ahorroProg > 0)
        	{
        		// HAY un ahorro programado
        		if (ahorroReal >= ahorroProg)
        		{
        			if (hoy.after(from) && hoy.before(to))
        			{
        				// Per�odo en curso, calcular presupuesto.
        				if (svcProvider.get().alcanzaPresupuesto(ingresos, ahorroProg, ahorroReal, hoy))
        				{
        					color = "green";
        					icono = "/images/ok.gif";
        				}
        				else
        				{
        					color = "yellow";
        					icono = "/images/warn.gif";
        				}
        			}
        			else
        			{
    					color = "green";
    					icono = "/images/ok.gif";
        			}
        		}
        		else
        		{
        			color = "red";
        			icono = "/images/wrong.gif";
        		}
        	}
        	else
        	{
        		// No hay ahorro programado, c�lculo b�sico.
        		if (ahorroReal > 0)
        		{
					color = "green";
					icono = "/images/ok.gif";
        		}
        		else
        		{
        			color = "red";
        			icono = "/images/wrong.gif";
        		}
        	}
	        
			this.saldo.add(AttributeModifier.append("class", "number bold no_border " + color));	        
		}
	}
	
	@SuppressWarnings("rawtypes")
	private WebMarkupContainer buildEvolution()
	{
		WebMarkupContainer evolution = new WebMarkupContainer("evolution");
		
		evolution.add(new NonCachingImage("image", new PropertyModel(this, "evolutionChart")));
		
		return evolution;
	}
	
	@SuppressWarnings("rawtypes")
	private WebMarkupContainer buildIncomeTops()
	{
		WebMarkupContainer incomeTops = new WebMarkupContainer("incomeTops");
		
		incomeTops.add(new NonCachingImage("image", new PropertyModel(this, "incomePie")));
		
		return incomeTops;
	}
	
	public IResource getIncomePie()
	{
		PieChartEgresosMensuales chart = new PieChartEgresosMensuales(this.getTopConceptosIncome());
		chart.setWidth(310);
		chart.setHeight(200);
		chart.setBackgroundColor(bgColor);
		return chart.getGraficoResource(this.getTopConceptosIncome());
	}
	
	public IResource getOutcomePie()
	{
		PieChartEgresosMensuales chart = new PieChartEgresosMensuales(this.getTopConceptosOutcome());
		chart.setWidth(310);
		chart.setHeight(200);
		chart.setBackgroundColor(bgColor);
		return chart.getGraficoResource(this.getTopConceptosOutcome());
	}
	
	public IResource getEvolutionChart()
	{
		LineChartMonthlySavings chart = new LineChartMonthlySavings(this.getMonthlySavings());
		chart.setWidth(750);
		chart.setHeight(140);
		chart.setBackgroundColor(bgColor);
		return chart.getImageResource();
	}
	
	public List<Object[]> getTopConceptosOutcome()
	{
		List<Object[]> items = this.getTopConceptos(-1);
		Integer topOutcome = this.getTop();
		
		if (topOutcome < items.size())
		{
			return items.subList(0, topOutcome);
		}
		else
		{
			return items;
		}
	}
	
	public List<Object[]> getTopConceptosIncome()
	{
		List<Object[]> items = this.getTopConceptos(1);
		Integer topIncome = this.getTop();
		
		if (topIncome < items.size())
		{
			return items.subList(0, topIncome);
		}
		else
		{
			return items;
		}
	}
	
	private Map<Date, Float> getMonthlySavings()
	{
		Comunidad comunidad = this.getUsuario().getComunidad();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, Integer.parseInt(month.getValue()) - 1);
		calendar.set(Calendar.YEAR, Integer.parseInt(year.getValue()));
		Date to = new LastDayOfMonth(calendar);
		
		calendar.add(Calendar.MONTH, (-1) * this.getEvolutionMonths() - 1);
		Date from = new FirstDayOfMonth(calendar);
		
		return svcProvider.get().getMonthlySavings(comunidad, from, to);
	}
	
	public List<ChartItem> getSavings()
	{
		Map<Date, Float> savings = this.getMonthlySavings();
		List<ChartItem> items = new ArrayList<ChartItem>();
		
		if (savings != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
			List<Date> dates = new ArrayList<Date>(savings.keySet());
			Collections.sort(dates, new Comparator<Date>()
			{
				public int compare(Date arg0, Date arg1) 
				{
					return arg0.compareTo(arg1) * (-1);
				}
			});
			
			for (Date date : dates)
			{
				String monthName = sdf.format(date);
				monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
				
				ChartItem item = new ChartItem(monthName, (double) savings.get(date));
				items.add(item);
			}
		}
		
		Integer evolutionMonths = this.getEvolutionMonths();
		
		if (items != null && items.size() > evolutionMonths)
		{
			return items.subList(0, evolutionMonths);
		}
		else
		{
			return items;
		}
	}
	
	public List<Object[]> getTopConceptos(Integer factor)
	{
		Comunidad comunidad = this.getUsuario().getComunidad();
		Integer mes = Integer.parseInt(month.getValue());
		Integer anio = Integer.parseInt(year.getValue());
		
		logger.debug("Consultando movimientos por factor para el mes "+ mes +" del a�o "+ anio +" (factor "+ factor +").");
		
		List<Movimiento> movimientos = svcProvider.get().retrieveByFactor(comunidad, mes, anio, factor);
		List<Object[]> items = new ArrayList<Object[]>();
		
		if (movimientos != null)
		{
			Map<Concepto, Double> totales = new HashMap<Concepto, Double>();
			
			for (Movimiento movimiento : movimientos)
			{
				Concepto concepto = movimiento.getConcepto();
				
				if (totales.containsKey(concepto))
				{
					totales.put(concepto, totales.get(concepto) + movimiento.getImporte());
				}
				else
				{
					totales.put(concepto, (double)movimiento.getImporte());
				}
			}

			for (Concepto concepto : totales.keySet())
			{
				items.add(new Object[]{concepto, totales.get(concepto)});				
			}
			
			Collections.sort(items, new Comparator<Object[]>() 
			{
				public int compare(Object[] o1, Object[] o2) 
				{
					return ((Double) o1[1]).compareTo((Double) o2[1]) * -1;
				}
			});
		}
		
		return items;
	}
	
	@SuppressWarnings("rawtypes")
	private WebMarkupContainer buildOutcomeTops()
	{
		WebMarkupContainer outcomeTops = new WebMarkupContainer("outcomeTops");
		
		outcomeTops.add(new NonCachingImage("image", new PropertyModel(this, "outcomePie")));

		return outcomeTops;
	}
	
	@SuppressWarnings("rawtypes")
	private WebMarkupContainer buildIncomeOutcome()
	{
		WebMarkupContainer incomeOutcome = new WebMarkupContainer("incomeOutcome");
		
		incomeOutcome.add(new NonCachingImage("image", new PropertyModel(this, "barChart")));
		
		return incomeOutcome;
	}

	public IResource getBarChart()
	{
		IncomeOutcomeBarGraph image = new IncomeOutcomeBarGraph("image", new Double[]{this.getIncome(), this.getOutcome()});
		image.setWidth(170);
		image.setHeight(200);
		image.setShowTitle(false);
		image.setBackgroundPaint(Color.WHITE);
		image.setChartBackgroundColor(bgColor);
		
		return image.getGraficoResource();
	}
	
	public String getIcono() 
	{
		return icono;
	}

	public void setIcono(String icono) 
	{
		this.icono = icono;
	}

	public Double getIncome() 
	{
		return income;
	}

	public void setIncome(Double income) 
	{
		this.income = income;
	}

	public Double getOutcome() 
	{
		return outcome;
	}

	public void setOutcome(Double outcome) 
	{
		this.outcome = outcome;
	}

	public void setSaldo(Double saldo)
	{		
	}
	
	public Double getSaldo()
	{
		return (income != null? income: 0) - (outcome != null? outcome: 0);
	}

	public void setTop(Integer topIncome) 
	{
		this.top = topIncome;
	}

	public Integer getTop() 
	{
		return this.top;
	}

	public Integer getEvolutionMonths() 
	{
		return this.getUsuario().getPrefIntValue(Name.EVOLUTION_QUANTITY, "6");
	}

	public void setEvolutionMonths(Integer evolutionMonths) 
	{
		this.getUsuario().setPreferencia(Name.EVOLUTION_QUANTITY, evolutionMonths);
	}

}
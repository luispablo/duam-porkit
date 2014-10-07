package ar.com.duam.porky.ui.pages.analysis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import ar.com.duam.api.date.FirstDayOfMonth;
import ar.com.duam.api.date.LastDayOfMonth;
import ar.com.duam.components.ui.DropDownChoiceMonths;
import ar.com.duam.components.ui.DropDownChoiceYears;
import ar.com.duam.porky.graphics.LineChartConceptosMensual;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.pages.PagePorkyBase;

import com.google.inject.Provider;

public class PageMonthlyEvolution extends PagePorkyBase
{
    private static Logger logger = Logger.getLogger(PageMonthlyEvolution.class);
    private static final long serialVersionUID = -8594822660743093669L;

    @Inject
    private Provider<IService> svcProvider;
    
    @Override
	public void renderHead(IHeaderResponse response) 
    {
    	PackageResourceReference cssFile = new PackageResourceReference(PagePorkyBase.class, "bootstrap.min.css");
    	CssHeaderItem cssItem = CssHeaderItem.forReference(cssFile);
    	response.render(cssItem);
	}
    
    @Override
    protected void init()
    {
		add(new FormMonthlyEvolution("form"));
        
        LineChartConceptosMensual chart = new LineChartConceptosMensual(Collections.<Concepto, Map<Date, Double>> emptyMap());
        add(chart.getImage("chart"));
    }

    class ConceptoWrapper
    {
    	private Concepto c;
    	private Boolean seleccionado = false;
    	
    	public ConceptoWrapper(Concepto c)
    	{
    		this.c = c;
    	}
    	
		public Concepto getC() 
		{
			return c;
		}
		
		public void setC(Concepto c) 
		{
			this.c = c;
		}
		
		public Boolean getSeleccionado() 
		{
			return seleccionado;
		}
		
		public void setSeleccionado(Boolean seleccionado) 
		{
			this.seleccionado = seleccionado;
		}    	
    }
    
    @SuppressWarnings("rawtypes")
	class FormMonthlyEvolution extends Form
    {
        private static final long serialVersionUID = -4286905972928075477L;
		private DropDownChoiceMonths fromMonth;
		private DropDownChoiceYears fromYear;
		private DropDownChoiceMonths toMonth;
		private DropDownChoiceYears toYear;
		private ListView conceptos;
		
        @SuppressWarnings("unchecked")
		public FormMonthlyEvolution(String id)
        {
            super(id);

            add(fromMonth = new DropDownChoiceMonths("fromMonth", getLocale()));
            add(fromYear = new DropDownChoiceYears("fromYear", getLocale()));
			add(toMonth = new DropDownChoiceMonths("toMonth", getLocale()));
			add(toYear = new DropDownChoiceYears("toYear", getLocale()));

            try
            {
                Usuario usuario = (Usuario) svcProvider.get().merge(getUsuario());
                List<Concepto> conceptosUsuario = new ArrayList<>(usuario.getComunidad().getConceptos());
                Collections.sort(conceptosUsuario, new Comparator<Concepto>() 
                {
					@Override
					public int compare(Concepto o1, Concepto o2) 
					{
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
                List<ConceptoWrapper> wrappers = new ArrayList<>();
                
                for (Concepto c : conceptosUsuario)
                {
                	wrappers.add(new ConceptoWrapper(c));
                }

                add(conceptos = new ListView("conceptos", wrappers)
                {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem item) 
					{
						ConceptoWrapper w = (ConceptoWrapper) item.getModelObject();
						item.add(new Label("nombre", w.getC().getNombre()));
						item.add(new CheckBox("selected", new PropertyModel(w, "seleccionado")));
					}                	
                });
            }
            catch (Exception exception)
            {
                e(exception);
            }
        }

        @SuppressWarnings("unchecked")
		@Override
        protected void onSubmit()
        {
			Calendar aux = Calendar.getInstance();
			aux.set(Calendar.YEAR, Integer.parseInt(fromYear.getValue()));
			aux.set(Calendar.MONTH, Integer.parseInt(fromMonth.getValue()) - 1);
			
			FirstDayOfMonth from = new FirstDayOfMonth(aux);
			
			aux.set(Calendar.YEAR, Integer.parseInt(toYear.getValue()));
			aux.set(Calendar.MONTH, Integer.parseInt(toMonth.getValue()) - 1);
			
			LastDayOfMonth to = new LastDayOfMonth(aux);
			
			List<Concepto> selected = new ArrayList<>();
			
			for (ConceptoWrapper w : (List<ConceptoWrapper>) conceptos.getList())
			{
				if (w.getSeleccionado())
				{
					selected.add(w.getC());
				}
			}
			
            logger.debug("Se van a consultar los totales desde "+ from +" hasta "+ to +" para "+ selected.size() +" conceptos.");

            getPage().remove("chart");
            
            try
            {
                Map<Concepto, Map<Date, Double>> totales = svcProvider.get().getTotalesMensuales(selected, from, to);

                for (Concepto concepto : totales.keySet())
                {
                    Map<Date, Double> auxi = totales.get(concepto);
                    logger.debug("Para el concepto "+ concepto.getNombre());

                    for (Date fecha : auxi.keySet())
                    {
                        logger.debug("Para la fecha "+ fecha +": "+ auxi.get(fecha));
                    }
                }

                LineChartConceptosMensual chart = new LineChartConceptosMensual(totales);
                getPage().add(chart.getImage("chart"));
            }
            catch (Exception exception)
            {
                e(exception);
            }
        }
    }
	
}
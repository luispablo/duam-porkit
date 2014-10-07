package ar.com.duam.porky.ui.pages.analysis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import ar.com.duam.api.date.FirstDayOfMonth;
import ar.com.duam.api.date.LastDayOfMonth;
import ar.com.duam.components.ui.DropDownChoiceMonths;
import ar.com.duam.components.ui.DropDownChoiceYears;
import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Movimiento;
import ar.com.duam.porky.model.Preferencia;
import ar.com.duam.porky.model.Preferencia.Name;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.SessionPorky;
import ar.com.duam.porky.ui.charts.ChartItem;
import ar.com.duam.porky.ui.charts.PieChartImage;
import ar.com.duam.porky.ui.filters.FilterMovimientoList;
import ar.com.duam.porky.ui.pages.PageMovimientoList;
import ar.com.duam.porky.ui.pages.PagePorkyBase;

import com.google.inject.Provider;

public class PageOutcomeAnalysis extends PagePorkyBase
{
    private static Logger logger = Logger.getLogger(PageOutcomeAnalysis.class);
    private static final String CANTIDAD_CONCEPTOS_POR_DEFECTO = "7";
    private static final long serialVersionUID = -1606066884856015005L;
    private String mes;
    private Integer anio;
    private String cantidadConceptos;
    private Label labelTotalEgresos;
    @SuppressWarnings("rawtypes")
	private ListView listViewTotales;
    private Map<String, Concepto> conceptos;

    @Inject
    private Provider<IService> svcProvider;
    
    @Override
	public void renderHead(IHeaderResponse response) 
    {
    	PackageResourceReference cssFile = new PackageResourceReference(PagePorkyBase.class, "bootstrap.min.css");
    	CssHeaderItem cssItem = CssHeaderItem.forReference(cssFile);
    	response.render(cssItem);
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    protected void init()
    {
        // Agregar el total de egresos del mes.
        this.agregarTotalMes();

        this.add(new FormOutcomeAnalysis("form"));

        this.listViewTotales = new ListView("listViewTotales", new ArrayList())
        {
			private static final long serialVersionUID = -6738413095138420570L;

			@Override
            protected void populateItem(ListItem item)
            {
                final ChartItem datos = (ChartItem) item.getModelObject();

                Label labelConcepto = new Label("labelConcepto", datos.getLabel());
                Link linkConcepto = new Link("linkConcepto")
                {
					private static final long serialVersionUID = 3684885031218489488L;

					@Override
                    public void onClick()
                    {
                        Concepto concepto = conceptos.get(datos.getLabel());
                        logger.debug("About to show detail of '"+ concepto +"'...");
                        showMovimientos(concepto);
                    }
                };
                linkConcepto.add(labelConcepto);

                item.add(linkConcepto);

                DecimalFormat format = new DecimalFormat("#0.00#");
                item.add(new Label("labelTotal", format.format(datos.getValue())));
            }
        };
        this.add(this.listViewTotales);

        try
        {
            PieChartImage pieChart = new PieChartImage("pieChart", new ArrayList<ChartItem>(), 500, 350);
            add(pieChart);
//            add(pieChart.getMap("pieMap"));

            Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            Integer year = Calendar.getInstance().get(Calendar.YEAR);

            this.mes = month.toString();
            this.anio = year;

            fillData(month, year);
        }
        catch (Exception exception)
        {
            e(exception);
        }
    }

    /**
     * <p>Configura los parámetros del filtro y redirige a la página de movi-
     * mientos.</p>
     */
    private void showMovimientos(Concepto concepto)
    {
        Integer month = new Integer(Integer.parseInt(this.mes) - 1);

        Calendar aux = Calendar.getInstance();
        aux.set(Calendar.MONTH, month);
        aux.set(Calendar.YEAR, anio);

        FirstDayOfMonth desde = new FirstDayOfMonth(aux);
        LastDayOfMonth hasta = new LastDayOfMonth(aux);

        SessionPorky session = (SessionPorky) Session.get();
        FilterMovimientoList filter = session.getFilter();

        filter.setConcepto(concepto);
        filter.setDesde(desde);
        filter.setHasta(hasta);

        this.setResponsePage(PageMovimientoList.class);
    }

    public String getMes()
    {
        return mes;
    }

    public void setMes(String mes)
    {
        this.mes = mes;
    }

    public Integer getAnio()
    {
        return anio;
    }

    public void setAnio(Integer anio)
    {
        this.anio = anio;
    }

    public String getCantidadConceptos()
    {
        return cantidadConceptos;
    }

    public void setCantidadConceptos(String cantidadConceptos)
    {
        this.cantidadConceptos = cantidadConceptos;
    }

    private void agregarTotalMes()
    {
        this.labelTotalEgresos = new Label("labelTotalEgresos", new Double(0.0).toString());
        this.add(this.labelTotalEgresos);
    }

    @SuppressWarnings({ "unchecked", "unused" })
	private void fillData(Integer month, Integer year)
            throws Exception
    {
        logger.info("Se arma la pagina para el mes " + month + " del anioo " + year + ".");

        Comunidad comunidad = (Comunidad) svcProvider.get().merge(getUsuario().getComunidad());

        List<Movimiento> movimientos = svcProvider.get().retrieveEgresosMes(comunidad, month, year);
        logger.info("En ese per�odo han habido " + movimientos.size() + " movimientos.");

        List<ChartItem> totales = sumarTotales(movimientos);
        listViewTotales.setList(totales);

        Double totalMes = total(totales);
        DecimalFormat format = new DecimalFormat("#0.00#");

        totales = limitarCantidad(totales);

        PageOutcomeAnalysis.this.remove("pieChart");
        PieChartImage pieChart = new PieChartImage("pieChart", totales, 500, 350);

        PageOutcomeAnalysis.this.add(pieChart);
    }

    private List<ChartItem> sumarTotales(List<Movimiento> movimientos)
            throws Exception
    {
        conceptos = new HashMap<String, Concepto>();
        Map<Concepto, Double> totales = new HashMap<Concepto, Double>();

        for (Movimiento movimiento : movimientos)
        {
            Concepto concepto = movimiento.getConcepto();

            if (totales.keySet().contains(concepto))
            {
                Double total = (Double) totales.get(concepto);
                totales.put(concepto, total + movimiento.getImporte());
            }
            else
            {
                conceptos.put(concepto.getNombre(), concepto);

                Double total = new Double(movimiento.getImporte());
                totales.put(concepto, total);
            }
        }

        List<ChartItem> output = new ArrayList<ChartItem>();

        for (final Concepto concepto : totales.keySet())
        {
            output.add(new ChartItem(concepto.getNombre(), totales.get(concepto))
            {
				private static final long serialVersionUID = -8614950845140804198L;

				@Override
                public void onClick()
                {
                    showMovimientos(concepto);
                }
            });
        }

        Collections.sort(output, new Comparator<ChartItem>()
        {

            public int compare(ChartItem arg0, ChartItem arg1)
            {
                ChartItem row0 = (ChartItem) arg0;
                ChartItem row1 = (ChartItem) arg1;

                return ((Double) row0.getValue()).compareTo((Double) row1.getValue()) * (-1);
            }
        });

        return output;
    }

    private List<ChartItem> limitarCantidad(List<ChartItem> totales)
    {
        List<ChartItem> output = new ArrayList<ChartItem>();

        Double totalOtros = 0d;

        int limite = Integer.parseInt(this.cantidadConceptos);
        int i = 0;

        for (ChartItem item : totales)
        {
            if (i < limite)
            {
                output.add(item);
                i++;
            }
            else
            {
                totalOtros += item.getValue();
            }
        }

        if (totalOtros > 0)
        {
            output.add(new ChartItem("Otros", totalOtros));
        }

        return output;
    }

    private Double total(List<ChartItem> totales)
    {
        Double total = 0d;

        for (ChartItem item : totales)
        {
            total += item.getValue();
        }

        return total;
    }

    @SuppressWarnings("rawtypes")
	class FormOutcomeAnalysis extends Form
    {
        private static final long serialVersionUID = 5142136165955363531L;

        private DropDownChoiceMonths mes;
        private DropDownChoiceYears anio;

        @SuppressWarnings("unchecked")
		public FormOutcomeAnalysis(String id)
        {
            super(id, new CompoundPropertyModel(PageOutcomeAnalysis.this));

            mes = new DropDownChoiceMonths("mes", PageOutcomeAnalysis.this.getLocale())
            {
				private static final long serialVersionUID = -2009171123065149585L;

				@Override
                protected boolean wantOnSelectionChangedNotifications()
                {
                    return false;
                }
            };
            mes.setNullValid(false);
            this.add(mes);

            anio = new DropDownChoiceYears("anio", PageOutcomeAnalysis.this.getLocale())
            {
				private static final long serialVersionUID = -5143565931032717717L;

				@Override
                protected boolean wantOnSelectionChangedNotifications()
                {
                    return false;
                }
            };
            anio.setNullValid(false);
            this.add(anio);

            try
            {
                Usuario usuario = (Usuario) svcProvider.get().merge(getUsuario());
                Preferencia cantidadConceptosUsuario = usuario.getPreferencia(Name.CANTIDAD_CONCEPTOS_GRAFICO);

                if (cantidadConceptosUsuario != null)
                {
                    PageOutcomeAnalysis.this.setCantidadConceptos(cantidadConceptosUsuario.getValor());
                }
                else
                {
                    PageOutcomeAnalysis.this.setCantidadConceptos(PageOutcomeAnalysis.CANTIDAD_CONCEPTOS_POR_DEFECTO);
                }
            }
            catch (Exception exception)
            {
                e(exception);
            }

            add(new TextField("cantidadConceptos"));
        }

        @Override
        protected void onSubmit()
        {
            PageOutcomeAnalysis.this.mes = mes.getValue();
            PageOutcomeAnalysis.this.anio = new Integer(Integer.parseInt(anio.getValue()));

            Integer month = new Integer(Integer.parseInt(mes.getValue()));
            Integer year = new Integer(Integer.parseInt(anio.getValue()));
            logger.info("Se van a consultar los egresos para el mes " + month + " del a�o " + year + ".");

            try
            {
                Usuario usuario = (Usuario) svcProvider.get().merge(getUsuario());
                Preferencia cantidadConceptos = usuario.getPreferencia(Name.CANTIDAD_CONCEPTOS_GRAFICO);
                String cantidadPagina = PageOutcomeAnalysis.this.cantidadConceptos;

                if (cantidadConceptos != null)
                {
                    if (Integer.parseInt(cantidadConceptos.getValor()) != Integer.parseInt(cantidadPagina))
                    {
                        cantidadConceptos.setValor(cantidadPagina);

                        svcProvider.get().merge(cantidadConceptos);
                    }
                }
                else
                {
                    cantidadConceptos = new Preferencia();
                    cantidadConceptos.setClave(Name.CANTIDAD_CONCEPTOS_GRAFICO.name());
                    cantidadConceptos.setValor(cantidadPagina);
                    cantidadConceptos.setUsuario(usuario);

                    svcProvider.get().merge(cantidadConceptos);
                }

                PageOutcomeAnalysis.this.fillData(month, year);
            }
            catch (Exception exception)
            {
                e(exception);
            }
        }
    }
}

/*
 * PageMovimientoList.java
 *
 * Created on 30/04/2008
 *
 * Copyright(c) 2007 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.pages;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import ar.com.duam.components.ui.PageLink;
import ar.com.duam.porky.graphics.IncomeOutcomeBarGraph;
import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Movimiento;
import ar.com.duam.porky.model.TipoConcepto;
import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.ApplicationPorky;
import ar.com.duam.porky.ui.SessionPorky;
import ar.com.duam.porky.ui.filters.FilterMovimientoList;
import ar.com.duam.porky.ui.panels.PanelFiltroMovimiento;

import com.google.inject.Provider;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PageMovimientoList extends PagePorkyBase
{

    private static Logger logger = Logger.getLogger(PageMovimientoList.class);
    private static final long serialVersionUID = 3715043548804185013L;
    private PageableListView<Movimiento> listViewMovimientos;
    private Label labelSaldo;
    private Double saldo;

    @Inject
    private Provider<IService> svcProvider;
    
    public Double getSaldo() 
    {
    	return saldo;
	}

	public void setSaldo(Double saldo) 
	{
		this.saldo = saldo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected void init()
    {
        try
        {
            this.add(labelSaldo = new Label("saldo", new PropertyModel(this, "saldo")));

            Link linkFecha = new Link("linkFecha")
            {
				private static final long serialVersionUID = -102118302866094649L;

				@Override
                public void onClick()
                {
                    FilterMovimientoList filter = ((SessionPorky) this.getSession()).getFilter();
                    filter.addOrderByField("fecha");
                    refreshList();
                }
            };
            this.add(linkFecha);

            Link linkDetalle = new Link("linkDetalle")
            {
				private static final long serialVersionUID = 8253982776872250166L;

				@Override
                public void onClick()
                {
                    FilterMovimientoList filter = ((SessionPorky) this.getSession()).getFilter();
                    filter.addOrderByField("detalle");
                    refreshList();
                }
            };
            this.add(linkDetalle);

            Link linkImporte = new Link("linkImporte")
            {
				private static final long serialVersionUID = 6464080328468900725L;

				@Override
                public void onClick()
                {
                    FilterMovimientoList filter = ((SessionPorky) this.getSession()).getFilter();
                    filter.addOrderByField("importe");
                    refreshList();
                }
            };
            this.add(linkImporte);

            ApplicationPorky app = (ApplicationPorky) this.getApplication();
            Integer maxRowsPerPage = Integer.parseInt(app.getInitParameter(ApplicationPorky.PropertiesKeys.MAX_ROWS_PER_PAGE));

            add(new Link("nuevo") 
    		{
				private static final long serialVersionUID = -4872041452345433203L;

				@Override
				public void onClick() 
				{
		            try
		            {
		                ar.com.duam.porky.model.Movimiento mov = new ar.com.duam.porky.model.Movimiento();
		                Comunidad comunidad = getUsuario().getComunidad();
		                mov.setComunidad((ar.com.duam.porky.model.Comunidad) svcProvider.get().find(ar.com.duam.porky.model.Comunidad.class, new Long(comunidad.getId())));
		                mov.setFecha(new Date());

		                setResponsePage(new PageMovimientoForm(mov));
		            }
		            catch (Exception ex)
		            {
		                e(ex);
		            }
				}
			});
            
            listViewMovimientos = new PageableListView<Movimiento>("listViewMovimientos", new ArrayList<Movimiento>(), maxRowsPerPage)
            {
				private static final long serialVersionUID = 3122022436367082112L;

				@Override
                protected void populateItem(ListItem item)
                {
                    int index = item.getIndex();

                    item.add(AttributeModifier.append("class", (index % 2 == 0 ? "even" : "odd")));

                    final Movimiento movimiento = (Movimiento) item.getModelObject();

                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
                    DecimalFormat decimaler = new DecimalFormat("#0.00#");

                    try
                    {
                        item.add(new Label("fecha", formatter.format(movimiento.getFecha())));
                        item.add(new Label("concepto", movimiento.getConcepto().getNombre()));

                        Label detalle = new Label("detalle", movimiento.getDetalleTruncated());
                        detalle.add(AttributeModifier.append("title", movimiento.getDetalle()));

                        item.add(detalle);

                        Double valorImporte = movimiento.getImporte() * new Double(movimiento.getConcepto().getTipoConcepto().getFactor());
                        Label importe = new Label("importe", decimaler.format(valorImporte));
                        String importeClass = (valorImporte > 0) ? "income" : "outcome";
                        importe.add(AttributeModifier.append("class", importeClass));

                        item.add(importe);

                        PageLink linkEditar = new PageLink<PageMovimientoForm>("linkEditar", PageMovimientoForm.class)
                        {
							private static final long serialVersionUID = -7047684386820395209L;

							protected Page getPageTo()
                            {
                                ar.com.duam.porky.model.Movimiento mov = svcProvider.get().find(ar.com.duam.porky.model.Movimiento.class, new Long(movimiento.getId()));

                                return new PageMovimientoForm(mov);
                            }
                        };
                        linkEditar.add(new Image("imageBirome", "imageBirome"));

                        Link linkBorrar = new Link("linkBorrar")
                        {
							private static final long serialVersionUID = -1603201600884327479L;

							public void onClick()
                            {
                                try
                                {
                                	svcProvider.get().remove(svcProvider.get().merge(movimiento));
                                    removeMovimiento(movimiento);
                                }
                                catch (Exception exception)
                                {
                                    logger.error("ERROR", exception);
                                    this.error(exception.getMessage());
                                }
                            }
                        };
                        linkBorrar.add(new Image("imageCruz", "imageCruz"));

                        final String confirmDeleteMessage = this.getLocalizer().getString("confirmDelete", PageMovimientoList.this);
                        linkBorrar.add(AttributeModifier.append("onclick", "if (!confirm(\'" + confirmDeleteMessage + "\')){ return false; };"));

                        item.add(linkEditar);
                        item.add(linkBorrar);
                    }
                    catch (Exception exception)
                    {
                        logger.error("ERROR", exception);
                        this.error(exception.getMessage());
                    }
                }
            };

            this.add(listViewMovimientos);

            this.refreshImage(0D, 0D);

            Comunidad comunidad = svcProvider.get().merge(getComunidad());

            List<Concepto> conceptos = new ArrayList<Concepto>(comunidad.getConceptos());
            Collections.sort(conceptos);
            
            List<TipoConcepto> tiposConcepto = svcProvider.get().findAll(TipoConcepto.class);

            this.add(new PanelFiltroMovimiento("panelFiltroMovimiento", conceptos, tiposConcepto)
            {
				private static final long serialVersionUID = -641789705717488244L;

				@Override
                public void onFilterApplied()
                {
                    refreshList();
                }
            });

            PagingNavigator navigator = new PagingNavigator("navigator", listViewMovimientos);
            this.add(navigator);
        }
        catch (Exception exception)
        {
        	logger.error("ERROR", exception);
            error(exception);
        }
    }

    @SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	private void refreshList()
    {
        try
        {
            SessionPorky session = (SessionPorky) this.getSession();
            FilterMovimientoList filter = session.getFilter();
            Comunidad comunidad = session.getUsuario().getComunidad();
            Date desde = filter.getDesde();
            Date hasta = filter.getHasta();

            Concepto concepto = null;
            TipoConcepto tipoConcepto = null;

            if (filter.getConcepto() != null)
            {
                concepto = svcProvider.get().find(Concepto.class, new Long(filter.getConcepto().getId()));
            }
            if (filter.getTipoConcepto() != null)
            {
            	tipoConcepto = svcProvider.get().find(TipoConcepto.class, new Long(filter.getTipoConcepto().getId()));
            }

            List<Movimiento> movimientos = svcProvider.get().findMovimientos(comunidad, desde, hasta, concepto, tipoConcepto, filter.getDetalle(), filter.getOrderFields());

            listViewMovimientos.setList(movimientos);

            Double ingresos = svcProvider.get().getTotalPorFactor(desde, hasta, comunidad, 1);
            Double egresos = svcProvider.get().getTotalPorFactor(desde, hasta, comunidad, -1);

            this.saldo = 0d;

            if (ingresos != null)
            {
                this.saldo += ingresos;
            }
            if (egresos != null)
            {
                this.saldo -= egresos;
            }

            labelSaldo.add(new AttributeModifier("class", true, new Model((saldo >= 0 ? "big_income" : "big_outcome"))));

            refreshImage(ingresos, egresos);
        }
        catch (Exception exception)
        {
            e(exception);
        }
    }

    /**
     * <p>Quita un movimiento del listado.</p>
     * <br>
     * 
     * @param movimiento
     */
    private void removeMovimiento(Movimiento movimiento)
    {
        listViewMovimientos.getList().remove(movimiento);
        this.refreshList();
    }

    /**
     * <p>Quita y vuelve a colocar la imagen con el gráfico de barras.</p>
     * <br>
     * 
     * @param totalIngresos
     * @param totalEgresos
     */
    private void refreshImage(Double totalIngresos, Double totalEgresos)
    {
        try
        {
            this.remove("imagen");
            remove("map");
        }
        catch (Exception exception)
        {
            // Esto puede fallar, y está bien la primera vez.
        }

        IncomeOutcomeBarGraph graph = new IncomeOutcomeBarGraph("imagen", new Double[]{totalIngresos, totalEgresos});

        this.add(graph.getImage());
    }

 }
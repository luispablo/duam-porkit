/*
 * PanelFiltroMovimiento.java
 *
 * Created on 30/04/2008
 *
 * Copyright(c) 2007 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.panels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.TipoConcepto;
import ar.com.duam.porky.ui.SessionPorky;
import ar.com.duam.porky.ui.filters.FilterMovimientoList;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public abstract class PanelFiltroMovimiento extends Panel
{
	private static final long serialVersionUID = 1820494932341086408L;
	private static Logger logger = Logger.getLogger(PanelFiltroMovimiento.class);
	
    private Date desde;
    private Date hasta;
    private Concepto concepto;
    private TipoConcepto tipoConcepto;
    private String detalle;

    public String getDetalle() 
    {
		return detalle;
	}

	public void setDetalle(String detalle) 
	{
		this.detalle = detalle;
	}

	public TipoConcepto getTipoConcepto() 
    {
		return tipoConcepto;
	}

	public void setTipoConcepto(TipoConcepto tipoConcepto) 
	{
		this.tipoConcepto = tipoConcepto;
	}

	public Concepto getConcepto()
    {
        return concepto;
    }

    public void setConcepto(Concepto concepto)
    {
        this.concepto = concepto;
    }

    public PanelFiltroMovimiento(String id, List<Concepto> conceptos, List<TipoConcepto> tiposConcepto)
    {
        super(id);

        FilterMovimientoList filter = ((SessionPorky) this.getSession()).getFilter();
        this.concepto = filter.getConcepto();
        this.tipoConcepto = filter.getTipoConcepto();

        this.add(new FormFiltroMovimiento("formFiltroMovimiento", conceptos, tiposConcepto));
    }

    private void doSearch()
            throws Exception
    {
        FilterMovimientoList filter = ((SessionPorky) this.getSession()).getFilter();

        filter.setDesde(desde);
        filter.setHasta(hasta);
        filter.setConcepto(concepto);
        filter.setTipoConcepto(this.tipoConcepto);
        
        if (this.detalle != null)
        {
            filter.setDetalle(this.detalle.replace('*', '%'));
        }
        else
        {
        	filter.setDetalle(null);
        }

        onFilterApplied();
    }

    /**
     * <p>This event is triggered when the submit button is pressed.</p>
     * <br>
     * 
     * @param movimientos The resulting list of objects.
     */
    public abstract void onFilterApplied();

    public Date getDesde()
    {
        return desde;
    }

    public void setDesde(Date desde)
    {
        this.desde = desde;
    }

    public Date getHasta()
    {
        return hasta;
    }

    public void setHasta(Date hasta)
    {
        this.hasta = hasta;
    }

    @SuppressWarnings("rawtypes")
	class FormFiltroMovimiento extends Form
    {
        private static final long serialVersionUID = 6365648349163405243L;

        public FormFiltroMovimiento(String id, List<Concepto> conceptos, List<TipoConcepto> tiposConcepto)
        {
            super(id);

            FilterMovimientoList filter = ((SessionPorky) this.getSession()).getFilter();

            desde = filter.getDesde();
            hasta = filter.getHasta();
            concepto = filter.getConcepto();
            tipoConcepto = filter.getTipoConcepto();

            DateTextField desde = new DateTextField("desde", "dd/MM/yyyy");
            desde.setModel(new PropertyModel<Date>(PanelFiltroMovimiento.this, "desde"));
            desde.setOutputMarkupId(false);
            this.add(desde);

            DateTextField hasta = new DateTextField("hasta", "dd/MM/yyyy");
            hasta.setModel(new PropertyModel<Date>(PanelFiltroMovimiento.this, "hasta"));
            hasta.setOutputMarkupId(false);
            this.add(hasta);

            try
            {
				ConceptosDropDownChoice ddConceptos = new ConceptosDropDownChoice("concepto", new ArrayList<Concepto>(conceptos), PanelFiltroMovimiento.this);
				ddConceptos.setNullValid(true);
                add(ddConceptos);

                TipoConceptoDropDownChoice ddTiposConcepto = new TipoConceptoDropDownChoice("tipoConcepto", tiposConcepto, PanelFiltroMovimiento.this);
                ddTiposConcepto.setNullValid(true);
                this.add(ddTiposConcepto);
                
                TextField detalle = new TextField<String>("detalle", new PropertyModel<String>(PanelFiltroMovimiento.this, "detalle"));
                this.add(detalle);
                
                doSearch();
            }
            catch (Exception exception)
            {
                logger.error("ERROR", exception);
                this.error(exception.getMessage());
            }
        }

        @Override
        protected void onSubmit()
        {
            try
            {
                doSearch();
            }
            catch (Exception exception)
            {
                logger.error("ERROR", exception);
                this.error(exception.getMessage());
            }
        }
    }

    class TipoConceptoDropDownChoice extends DropDownChoice<TipoConcepto>
    {
		private static final long serialVersionUID = -3593510529924122343L;

		public TipoConceptoDropDownChoice(String id, List<TipoConcepto> tiposConcepto, Panel panel)
    	{
    		super(id, tiposConcepto, new TipoConceptoChoiceRenderer());
    		this.setModel(new PropertyModel<TipoConcepto>(panel, "tipoConcepto"));
    	}
    }
    
    class ConceptosDropDownChoice extends DropDownChoice<Concepto>
    {
        private static final long serialVersionUID = -48611221026296263L;

        public ConceptosDropDownChoice(String id, List<Concepto> conceptos, Panel panel)
        {
            super(id, conceptos, new ConceptoChoiceRenderer());
            this.setModel(new PropertyModel<Concepto>(panel, "concepto"));
        }

        @Override
        protected boolean wantOnSelectionChangedNotifications()
        {
            return false;
        }
    }
    
    class TipoConceptoChoiceRenderer extends ChoiceRenderer<TipoConcepto>
    {
		private static final long serialVersionUID = -3927707668619973892L;
    	
        @Override
        public String getDisplayValue(TipoConcepto tc)
        {
            return tc.getNombre();
        }
    }
    
    class ConceptoChoiceRenderer extends ChoiceRenderer<Concepto>
    {
        private static final long serialVersionUID = 6080672754713829162L;

        public ConceptoChoiceRenderer()
        {
        }

        @Override
        public String getDisplayValue(Concepto c)
        {
            return c.getNombreTruncated();
        }
    }
}
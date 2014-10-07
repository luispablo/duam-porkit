package ar.com.duam.porky.wicket.forms;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Movimiento;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.pages.PageMovimientoList;
import ar.com.duam.porky.ui.pages.PagePorkyBase;

import com.google.inject.Provider;

@SuppressWarnings("rawtypes")
public class MovimientoForm extends Form
{

    private static Logger logger = Logger.getLogger(MovimientoForm.class);
    private static final long serialVersionUID = 7958871120053088086L;
    private Page movimientosPage;
    public Concepto concepto;

    @Inject
    private Provider<IService> svcProvider;
    
    @SuppressWarnings({ "unchecked" })
	public MovimientoForm(String id, Movimiento movimiento, Page movimientosPage)
    {
        super(id);
        this.setMovimientosPage(movimientosPage);

        try
        {
            if (movimiento.getDetalle() == null || movimiento.getDetalle().trim().length() == 0)
            {
                movimiento.setDetalle("-");
            }
            this.setModel(new CompoundPropertyModel(movimiento));

            DateTextField fecha = new DateTextField("fecha", "dd/MM/yyyy");
            
            Usuario usuario = (Usuario) svcProvider.get().merge(((PagePorkyBase) getPage()).getUsuario());

            this.add(fecha);
            this.add(new ConceptosDropDownChoice("conceptosSelect", new ArrayList(usuario.getComunidad().getConceptos()), movimiento).setRequired(true));
            this.add(new TextField("detalle", String.class).setRequired(true));
            this.add(new TextField("importe", Double.class).setRequired(true));
        }
        catch (Exception ex)
        {
            logger.error("EXCEPTION", ex);
        }
    }

    @Override
    protected void onSubmit()
    {
        try
        {
            Movimiento movimiento = (Movimiento) this.getModelObject();

            Usuario usuario = (Usuario) svcProvider.get().merge(((PagePorkyBase) getPage()).getUsuario());
            movimiento.setComunidad(usuario.getComunidad());

            svcProvider.get().merge(movimiento);

            this.setResponsePage(new PageMovimientoList());
        }
        catch (Exception exception)
        {
            logger.error("ERROR", exception);
        }
    }

    class ConceptosDropDownChoice extends DropDownChoice
    {
        private static final long serialVersionUID = -3017479262127596285L;

        @SuppressWarnings("unchecked")
		public ConceptosDropDownChoice(String id, List conceptos, Movimiento movimiento)
        {
            super(id, conceptos, new ConceptoChoiceRenderer());

            setModel(new PropertyModel(movimiento, "concepto"));
        }

        @Override
        protected boolean wantOnSelectionChangedNotifications()
        {
            return false;
        }

        @Override
        public void onSelectionChanged(Object newSelection)
        {
        }
    }

    class ConceptoChoiceRenderer extends ChoiceRenderer
    {
        private static final long serialVersionUID = -3082489892901531248L;

        public ConceptoChoiceRenderer()
        {
        }

        @Override
        public String getDisplayValue(Object object)
        {
            Concepto concepto = (Concepto) object;
            return concepto.getNombre();
        }
    }

    public void setConcepto(Concepto concepto)
    {
        this.concepto = concepto;
    }

    public Page getMovimientosPage()
    {
        return movimientosPage;
    }

    public void setMovimientosPage(Page movimientosPage)
    {
        this.movimientosPage = movimientosPage;
    }
}

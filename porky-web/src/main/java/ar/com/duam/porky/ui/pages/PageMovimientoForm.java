package ar.com.duam.porky.ui.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Movimiento;
import ar.com.duam.porky.services.IService;

import com.google.inject.Provider;

public class PageMovimientoForm extends PagePorkyBase
{

    private static Logger log = Logger.getLogger(PageMovimientoForm.class);
    private static final long serialVersionUID = -1802824248081299691L;

    @Inject
    private Provider<IService> svcProvider;
    
    @SuppressWarnings("rawtypes")
	public PageMovimientoForm(final Movimiento movimiento)
    {
        Form form = new Form("form")
        {
			private static final long serialVersionUID = -471997113239921813L;

			@Override
			protected void onSubmit() 
			{
                svcProvider.get().merge(movimiento);
                setResponsePage(new PageMovimientoList());
			}
        };
        add(form);

        try
        {
            movimiento.getDetalle();
            PropertyModel p = new PropertyModel(movimiento, "fecha");

            log.warn("fecha: " + p.getObjectClass());

            TextField<Date> fecha = new TextField<>("fecha", new PropertyModel<Date>(movimiento, "fecha"));
            fecha.setRequired(true);
            form.add(fecha);

            Comunidad comunidad = (Comunidad) svcProvider.get().merge(getComunidad());
            List<Concepto> conceptos = new ArrayList<Concepto>(comunidad.getConceptos());
            Collections.sort(conceptos);

            final TextField<Float> importe = new TextField<>("importe", new PropertyModel<Float>(movimiento, "importe"));
            importe.setOutputMarkupId(true);
            importe.setRequired(true);
            // Se agrega este comportamiento para que el importe "viaje" por si se setea antes del concepto.
            importe.add(new AjaxFormComponentUpdatingBehavior("onblur") 
            {
				private static final long serialVersionUID = -7054031289342761116L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) 
				{
				}
			});
            form.add(importe);
            
            DropDownChoice<Concepto> concepto = new DropDownChoice<>("concepto", new PropertyModel<Concepto>(movimiento, "concepto"), conceptos);
            concepto.add(new AjaxFormComponentUpdatingBehavior("onchange") 
            {
				private static final long serialVersionUID = -7054031289342761116L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) 
				{
					movimiento.updateImporteFromConcepto();
					target.add(importe);
				}
			});
            concepto.setChoiceRenderer(new ChoiceRenderer<Concepto>("nombre", "id"));
            concepto.setRequired(true);
            form.add(concepto);

            TextArea<String> detalle = new TextArea<>("detalle", new PropertyModel<String>(movimiento, "detalle"));
            detalle.setRequired(true);
            form.add(detalle);
        }
        catch (Exception ex)
        {
        	e(ex);
        }
    }

}
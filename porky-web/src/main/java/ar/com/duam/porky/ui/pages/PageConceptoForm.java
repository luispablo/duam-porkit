package ar.com.duam.porky.ui.pages;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.google.inject.Provider;

import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.TipoConcepto;
import ar.com.duam.porky.services.IService;

public class PageConceptoForm extends PagePorkyBase
{
    private static final long serialVersionUID = -8582257260082199864L;
    private Concepto concepto;

    @Inject
    private Provider<IService> svcProvider;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public PageConceptoForm(final Concepto pConcepto)
    {
    	this.concepto = pConcepto;
    	
        Form form = new Form("form")
        {
			private static final long serialVersionUID = 4151773701753557409L;

			@Override
            protected void onSubmit()
            {
                concepto = (Concepto) svcProvider.get().merge(concepto);
                
                info(getString("saved"));
                this.setResponsePage(PageConceptoList.class);
            }
        };
        add(form);

        form.add(new Button("save"));
        
        List<TipoConcepto> tipos = svcProvider.get().findAll(TipoConcepto.class); 

        try
        {
            DropDownChoice<TipoConcepto> tipoConcepto = new DropDownChoice<TipoConcepto>("tipoConcepto", new PropertyModel<TipoConcepto>(concepto, "tipoConcepto"), tipos);
            tipoConcepto.setChoiceRenderer(new ChoiceRenderer<TipoConcepto>("nombre", "id"));
            tipoConcepto.setRequired(true);
            form.add(tipoConcepto);

            TextField<String> nombre = new TextField<String>("nombre", new PropertyModel<String>(concepto, "nombre"));
            nombre.setRequired(true);
            form.add(nombre);
            
            form.add(new TextField<Float>("valorPorDefecto", new PropertyModel<Float>(concepto, "valorPorDefecto")));
        }
        catch (Exception exception)
        {
        	e(exception);
        }
    }
            
}
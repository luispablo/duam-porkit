package ar.com.duam.porky.ui.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

import ar.com.duam.components.ui.PageLink;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.ApplicationPorky;
import ar.com.duam.porky.ui.ApplicationPorky.PropertiesKeys;

import com.google.inject.Provider;

public class PageConceptoList extends PagePorkyBase
{

    private static Logger logger = Logger.getLogger(PageConceptoList.class);
    private static final long serialVersionUID = -3653017137445106624L;
    private PageableListView<Concepto> list;
    
    @Inject
    private Provider<IService> svcProvider;
    
    public PageConceptoList()
    {
    }

    @SuppressWarnings("unchecked")
    private void fillTable() throws Exception
    {
        try
        {
            list.getList().clear();

            List<Concepto> conceptos = svcProvider.get().findAll(Concepto.class);
            Collections.sort(conceptos, new Comparator<Concepto>() 
            {
				@Override
				public int compare(Concepto o1, Concepto o2) 
				{
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
            list.setList(conceptos);
        }
        catch (Exception exception)
        {
            logger.error("ERROR", exception);
        }
    }

    class AgregarButton extends Button
    {
        private static final long serialVersionUID = 6181855780094254834L;

        public AgregarButton(String id)
        {
            super(id);
        }

        @Override
        public void onSubmit()
        {
            try
            {
                setResponsePage(new PageConceptoForm(new Concepto(getComunidad())));
            }
            catch (Exception ex)
            {
                logger.error("EXCEPTION", ex);
            }
        }
    }

    @SuppressWarnings("rawtypes")
	@Override
    protected void init()
    {
        ApplicationPorky app = (ApplicationPorky) this.getApplication();
        Integer maxRowsPerPage = Integer.parseInt(app.getInitParameter(PropertiesKeys.MAX_ROWS_PER_PAGE));

        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        this.list = new PageableListView<Concepto>("filas", new ArrayList<Concepto>(), maxRowsPerPage)
        {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("serial")
			protected void populateItem(ListItem item)
            {
                final Concepto concepto = (Concepto) item.getModelObject();

                int index = item.getIndex();

                item.add(AttributeModifier.append("class", (index % 2 == 0 ? "even" : "odd")));

                try
                {
                    PageLink linkEditar = new PageLink<PageConceptoForm>("linkEditar", PageConceptoForm.class)
                    {
						private static final long serialVersionUID = -7713623789210813115L;

						protected Page getPageTo()
                        {
                            return new PageConceptoForm(concepto);
                        }
                    };

                    Link linkBorrar = new Link("linkBorrar")
                    {

                        public void onClick()
                        {
                            try
                            {
                            	svcProvider.get().merge(concepto);
                                svcProvider.get().remove(concepto);
                                
                                PageConceptoList.this.fillTable();
                            }
                            catch (Exception ex)
                            {
                                logger.error("ERROR", ex);
                            }
                        }
                    };
                    linkBorrar.add(new Image("imageCruz", "imageCruz"));

                    final String confirmDeleteMessage = this.getLocalizer().getString("confirmDelete", PageConceptoList.this);
                    linkBorrar.add(AttributeModifier.append("onclick", "if (!confirm(\'" + confirmDeleteMessage + "\')){ return false; };"));

                    linkEditar.add(new Image("imageBirome", "imageBirome"));

                    item.add(new Label("nombre", concepto.getNombre()));
                    item.add(linkEditar);
                    item.add(linkBorrar);

                }
                catch (Exception ex)
                {
                    logger.error("ERROR", ex);
                }
            }
        };

        try
        {
            container.add(this.list);
            add(new PagingNavigator("navigator", list));

            Form form = new Form("dummy");
            form.add(new AgregarButton("agregarButton"));
            add(form);
            
            fillTable();            
        }
        catch (Exception ex)
        {
            logger.error("ERROR", ex);
        }        
    }
}
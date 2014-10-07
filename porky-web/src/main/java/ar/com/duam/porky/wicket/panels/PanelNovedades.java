package ar.com.duam.porky.wicket.panels;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import ar.com.duam.porky.model.Novedad;

public class PanelNovedades extends Panel
{

    private static Logger logger = Logger.getLogger(PanelNovedades.class);
    private static final long serialVersionUID = 293727923180169175L;

    @Inject
    private EntityManager em;
    
    public PanelNovedades(String id)
    {
        super(id);

        this.init();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void init()
    {
        try
        {
            List<Novedad> novedades = em.createQuery("from Novedad").getResultList();

            ListView listViewNovedades = new ListView("listViewNovedades", novedades)
            {
				private static final long serialVersionUID = 1364029091939333916L;

				protected void populateItem(ListItem item)
                {
                    Novedad novedad = (Novedad) item.getModelObject();
                    SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");

                    item.add(new Label("labelFecha", format.format(novedad.getFecha())));
                    item.add(new Label("labelDetalle", novedad.getDetalle()));
                }
            };

            this.add(listViewNovedades);
        }
        catch (Exception exception)
        {
            logger.error("ERROR", exception);
            this.error(exception.getMessage());
        }
    }
}
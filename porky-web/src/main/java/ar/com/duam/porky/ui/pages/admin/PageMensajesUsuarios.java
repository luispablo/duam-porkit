/*
 * PageMensajesUsuarios.java
 *
 * Created on 21/05/2008
 *
 * Copyright(c) 2007 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.pages.admin;

import ar.com.duam.porky.model.Mensaje;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.ui.pages.PagePorkyBase;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PageMensajesUsuarios extends PagePorkyBase
{
    private static Logger logger = Logger.getLogger(PageMensajesUsuarios.class);
    private static final long serialVersionUID = 2346894337435838623L;

    @Inject
    private EntityManager em;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    protected void init()
    {
        try
        {
            List<Mensaje> mensajes = em.createQuery("from Mensaje").getResultList(); 

            ListView listViewMensajes = new ListView("listViewMensajes", mensajes)
            {
				private static final long serialVersionUID = 7222072016249230628L;

				@Override
                protected void populateItem(ListItem item)
                {
                    try
                    {
                        Mensaje mensaje = (Mensaje) item.getModelObject();
                        Usuario usuario = mensaje.getUsuario();

                        item.add(new Label("apellido", usuario.getApellido()));
                        item.add(new Label("nombre", usuario.getNombre()));
                        item.add(new Label("comunidad", usuario.getComunidad().getNombre()));
                        item.add(new Label("fecha", mensaje.getFecha().toString()));
                        item.add(new Label("texto", mensaje.getTexto()));
                    }
                    catch (Exception exception)
                    {
                        logger.error("ERROR", exception);
                        this.error(exception.getMessage());
                    }
                }
            };

            this.add(listViewMensajes);
        }
        catch (Exception exception)
        {
            e(exception);
        }
    }
}
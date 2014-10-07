/*
 * PageUserRanking.java
 *
 * Created on 14/05/2008
 *
 * Copyright(c) 2007 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.pages.admin;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.pages.PagePorkyBase;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PageUserRanking extends PagePorkyBase
{
    private static final long serialVersionUID = 8454835883897041639L;

    @Inject
    private IService service;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    protected void init()
    {
        try
        {
            List<String[]> rows = service.userRanking();
                    //UsuarioPeer.userRanking();

            ListView listViewRows = new ListView("listViewRows", rows)
            {
				private static final long serialVersionUID = -4462218141843817014L;

				@Override
                protected void populateItem(ListItem item)
                {
                    String[] row = (String[]) item.getModelObject();

                    item.add(new Label("apellido", row[0]));
                    item.add(new Label("nombre", row[1]));
                    item.add(new Label("comunidad", row[2]));
                    item.add(new Label("cantidad", row[3]));
                    item.add(new Label("ultimo", row[4]));
                }
            };

            this.add(listViewRows);
        }
        catch (Exception exception)
        {
            e(exception);
        }
    }
}
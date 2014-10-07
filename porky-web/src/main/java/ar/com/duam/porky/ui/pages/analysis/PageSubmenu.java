/*
 * PageSubmenu.java
 *
 * Created on 07/02/2009
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.pages.analysis;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;

import ar.com.duam.components.ui.PageLink;
import ar.com.duam.porky.ui.pages.PagePorkyBase;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PageSubmenu extends PagePorkyBase
{
	private static final long serialVersionUID = 4167703619110241171L;

    @Override
	public void renderHead(IHeaderResponse response) 
    {
    	PackageResourceReference cssFile = new PackageResourceReference(PagePorkyBase.class, "bootstrap.min.css");
    	CssHeaderItem cssItem = CssHeaderItem.forReference(cssFile);
    	response.render(cssItem);
	}
	
	@Override
    protected void init()
    {
        add(new PageLink<PageOutcomeAnalysis>("linkOutcomeAnalysis", PageOutcomeAnalysis.class));
        add(new PageLink<PageMonthlyEvolution>("linkMonthlyEvolution", PageMonthlyEvolution.class));
    }

}
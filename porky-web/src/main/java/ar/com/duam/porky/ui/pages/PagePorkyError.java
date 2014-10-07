package ar.com.duam.porky.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebPage;

public class PagePorkyError extends WebPage 
{
	private static final long serialVersionUID = 5045162193997688147L;
	private static final Logger log = Logger.getLogger(PagePorkyError.class);
	
	public PagePorkyError(Exception ex)
	{
		log.error("ERROR", ex);
	}

	@Override
	public boolean isErrorPage() 
	{
		return true;
	}
	
}
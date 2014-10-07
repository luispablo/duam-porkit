package ar.com.duam.components.ui;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

public class PageLink<T extends WebPage> extends Link<T>
{
	private static final long serialVersionUID = -1905426564262863074L;
	private static final Logger log = Logger.getLogger(PageLink.class);
	
	private Class<T> clazz;

	public PageLink(String id, Class<T> clazz)
	{
		super(id);
		this.clazz = clazz;
	}

	protected Page getPageTo()
	{
		try 
		{
			return clazz.newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e) 
		{
			log.error("No se puede instanciar la página", e);
			return null;
		}
	}

	@Override
	public void onClick() 
	{
		setResponsePage(getPageTo());
	}

}

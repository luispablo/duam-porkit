package ar.com.duam.porky.ui.pages;

public class PagePorkySessionExpired extends PageLogin 
{
	private static final long serialVersionUID = 4367363815064946570L;

	public PagePorkySessionExpired()
	{
		super();
		this.info(this.getString("sessionExpired"));
	}
	
}
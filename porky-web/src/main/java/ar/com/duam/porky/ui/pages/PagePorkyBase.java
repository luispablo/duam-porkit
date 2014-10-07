/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.duam.porky.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.resource.PackageResourceReference;

import ar.com.duam.components.ui.PageLink;
import ar.com.duam.dao.DataProvider;
import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.ui.SessionPorky;
import ar.com.duam.porky.ui.pages.analysis.PageSubmenu;

/**
 *
 * @author Luis Pablo
 */
public abstract class PagePorkyBase extends WebPage
{
	private static final long serialVersionUID = 1317353724886799790L;

	private static final Logger log = Logger.getLogger(PagePorkyBase.class);
	
	private String from;
	private String tos;
	private String comments;

	@Override
	protected void onConfigure() 
	{
		AuthenticatedWebApplication app = (AuthenticatedWebApplication) Application.get();
		
		//if user is not signed in, redirect him to sign in page
		if(!AuthenticatedWebSession.get().isSignedIn())
		{
			log.debug("-- No logueado, redirigiendo al login...");
			app.restartResponseAtSignInPage();
		}
		else
		{
			log.debug("-- Usuario logueado OK!");
		}
	}
	
    @Override
	public void renderHead(IHeaderResponse response) 
    {
    	PackageResourceReference cssFile = new PackageResourceReference(this.getClass(), "bootstrap.min.css");
    	CssHeaderItem cssItem = CssHeaderItem.forReference(cssFile);
    	response.render(cssItem);
	}
	
    @SuppressWarnings("rawtypes")
	public PagePorkyBase()
    {
        final SessionPorky session = (SessionPorky) this.getSession();

        add(new FeedbackPanel("feedbackPanel"));
        
        Usuario usuario = session.getUsuario();
        
        if (usuario == null)
        {
            this.setResponsePage(new PageLogin());
        }
        else
        {
            this.add(new PageLink<PageHome>("linkHome", PageHome.class));
            this.add(new PageLink<PageMovimientoList>("linkMovimientos", PageMovimientoList.class));
            this.add(new PageLink<PageSubmenu>("linkAnalisis", PageSubmenu.class));
            this.add(new PageLink<PageConceptoList>("linkConceptos", PageConceptoList.class));
            this.add(new PageLink<PageUsuarioForm>("linkUsuario", PageUsuarioForm.class));
            this.add(new Link("linkLogout")
            {
				private static final long serialVersionUID = 4050964143528582798L;

				@Override
                public void onClick()
                {
                    SessionPorky session = (SessionPorky) this.getSession();
                    session.setUsuario(null);

                    this.setResponsePage(new PageLogin());
                }
            });

            this.init();
        }
    }

    protected void e(Exception e)
    {
    	log.error("Error", e);
    	error(e.getMessage());
    }
    
	protected void init(){};

    protected Page getPreviousPage()
    {
        return ((SessionPorky) getSession()).getPreviousPage();
    }

    protected Comunidad getComunidad()
    {
        return getUsuario().getComunidad();
    }

    public Usuario getUsuario()
    {
        return ((SessionPorky) getSession()).getUsuario();
    }

    public DataProvider getDataProvider()
    {
        return null;
    }

	public String getFrom() 
	{
		if (this.from == null)
		{
			Usuario usuario = this.getUsuario();
			this.from = usuario.getNombre();
		}
		
		return from;
	}

	public void setFrom(String from) 
	{
		this.from = from;
	}

	public String getTos() 
	{
		return tos;
	}

	public void setTos(String tos) 
	{
		this.tos = tos;
	}

	public String getComments() 
	{
		return comments;
	}

	public void setComments(String comments) 
	{
		this.comments = comments;
	}

}
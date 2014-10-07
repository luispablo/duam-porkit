package ar.com.duam.porky.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import ar.com.duam.porky.ui.pages.PageHome;
import ar.com.duam.porky.ui.pages.PageLogin;
import ar.com.duam.porky.ui.pages.PagePorkyError;
import ar.com.duam.porky.ui.pages.PagePorkySessionExpired;
import ar.com.duam.porky.ui.pages.admin.PageMensajesUsuarios;
import ar.com.duam.porky.ui.pages.admin.PageUserRanking;
import ar.com.duam.porky.ui.pages.analysis.PageSubmenu;

public class ApplicationPorky extends AuthenticatedWebApplication
{

    private Logger logger = Logger.getLogger(ApplicationPorky.class);

    public static enum PropertiesKeys
    {

        TORQUE_PROPERTIES_FILE_NAME,
        LOG4J_PROPERTIES_FILE_NAME,
        MAX_ROWS_PER_PAGE
    };

    @Override
    protected void init()
    {
    	super.init();
    	
    	// Configuración para Guice + Wicket
    	final GuiceComponentInjector injector = new GuiceComponentInjector(this, new PorkyServletModule());
    	getComponentInstantiationListeners().add(injector);
    	
    	getApplicationSettings().setPageExpiredErrorPage(PagePorkySessionExpired.class);
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getDebugSettings().setAjaxDebugModeEnabled(false);        
        getRequestCycleListeners().add(new AbstractRequestCycleListener() 
        {        	
			@Override
			public void onBeginRequest(RequestCycle cycle) 
			{
				if (Session.exists()) 
				{
					injector.inject(Session.get());
		        }
			}

			@Override
			public IRequestHandler onException(RequestCycle cycle, Exception ex) 
			{
		    	if (ex instanceof PageExpiredException)
		    	{
					return new RenderPageRequestHandler(new PageProvider(new PagePorkySessionExpired()));
		    	}
		    	else
		    	{
					return new RenderPageRequestHandler(new PageProvider(new PagePorkyError(ex)));
		    	}				
			}        	
		});
        
        try
        {
            String prefix = this.getServletContext().getRealPath("/WEB-INF/");

            String log4jPropertiesFileName = this.getInitParameter(PropertiesKeys.LOG4J_PROPERTIES_FILE_NAME.name());

            if (log4jPropertiesFileName == null)
            {
                logger.error("Missing init parameter [" + PropertiesKeys.LOG4J_PROPERTIES_FILE_NAME.name() + "]");
            }

            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

            System.setProperty("log.dir", prefix + "/logs/");
            System.setProperty("date", format.format(today));

            System.out.println("Configurando log4j con el archivo '" + prefix + "/" + log4jPropertiesFileName + "'.");
            PropertyConfigurator.configureAndWatch(prefix + "/" + log4jPropertiesFileName);

            mountPage("/userRanking", PageUserRanking.class);
            mountPage("/mensajes", PageMensajesUsuarios.class);
            mountPage("/analysis", PageSubmenu.class);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * <p>Returns a given parameter stored in <code>web.xml</code> file.</p>
     * <br>
     * 
     * @param key The parameter name.
     * @return String
     */
    public String getInitParameter(PropertiesKeys key)
    {
        return this.getInitParameter(key.name());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getHomePage()
    {
        return PageHome.class;
    }

	@Override
	protected Class<? extends WebPage> getSignInPageClass() 
	{
		return PageLogin.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() 
	{
		return SessionPorky.class;
	}

}
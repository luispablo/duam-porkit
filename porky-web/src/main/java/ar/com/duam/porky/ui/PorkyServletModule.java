package ar.com.duam.porky.ui;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import ar.com.duam.porky.ws.ComunidadesResource;
import ar.com.duam.porky.ws.ConceptosResource;
import ar.com.duam.porky.ws.MovimientosResource;
import ar.com.duam.porky.ws.UsuariosResource;

import com.google.inject.Scopes;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class PorkyServletModule extends ServletModule 
{

	@Override
	protected void configureServlets() 
	{
		install(new JpaPersistModule("porky"));
		filter("/*").through(PersistFilter.class);
		
		// Configuración web services.
		bind(ComunidadesResource.class);
		bind(ConceptosResource.class);
		bind(MovimientosResource.class);
		bind(UsuariosResource.class);
		bind(JacksonJaxbJsonProvider.class).in(Scopes.SINGLETON);		
		serve("/ws/*").with(GuiceContainer.class);		
	}

}

package ar.com.duam.porky.ws;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Concepto;

import com.google.inject.Inject;

@Path("comunidades")
@Produces(MediaType.APPLICATION_JSON)
public class ComunidadesResource 
{
	@Inject EntityManager em;
	
	@GET @Path("{id}/conceptos")
	public Concepto[] conceptos(@PathParam("id") Long id)
	{
		Comunidad comu = em.find(Comunidad.class, id);
		
		return comu.getConceptos().toArray(new Concepto[0]); 
	}
	
}

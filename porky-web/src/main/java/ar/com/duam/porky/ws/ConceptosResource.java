package ar.com.duam.porky.ws;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ar.com.duam.porky.model.Usuario;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path("/conceptos")
@Produces(MediaType.APPLICATION_JSON)
public class ConceptosResource 
{
	@Inject
	private Provider<EntityManager> entityManager;
	
	@GET
	@Path("{username}")
	public Response conceptos(@PathParam("username") String username)
	{
		EntityManager em = entityManager.get();
		
		Query query = em.createQuery("from Usuario where nombreUsuario = :username");
		query.setParameter("username", username);
		
		Usuario usuario = (Usuario) query.getSingleResult();
		
		if (usuario != null)
		{
			query = em.createQuery("from Concepto where comunidad = :comunidad");
			query.setParameter("comunidad", usuario.getComunidad());
			
			return Response.status(Response.Status.OK).entity(query.getResultList()).build();
		}
		else
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
}

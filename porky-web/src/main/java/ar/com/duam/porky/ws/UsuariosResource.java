package ar.com.duam.porky.ws;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;
import ar.com.duam.security.Crypto;

import com.google.inject.Provider;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
public class UsuariosResource 
{
	private static final Logger log = Logger.getLogger(UsuariosResource.class);
	
	@Inject
	private Provider<IService> svcProvider;

	@POST
	@Path("/login")
	public Response login(@FormParam("username") String username, @FormParam("password") String password)
	{
		log.debug("Buscando el usuario con nombre de usuario '"+ username +"'");
		Usuario usuario = svcProvider.get().findUsuarioByUsername(username);
		
		if (usuario != null)
		{
			try 
			{
				String cryptedPassword = Crypto.encrypt(password);
				log.debug("Comparando contraseña provista '"+ cryptedPassword +"' con la guardada en base '"+ usuario.getClave() +"'");
				
				if (cryptedPassword.equals(usuario.getClave()))
				{
					return Response.status(Status.OK).entity(usuario).build();
				}
				else
				{
					return Response.status(Status.NOT_ACCEPTABLE).build();
				}
			} 
			catch (Exception e) 
			{
				log.error("Error al encriptar", e);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
			}			
		}
		else
		{
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
}

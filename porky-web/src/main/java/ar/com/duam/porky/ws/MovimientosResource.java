package ar.com.duam.porky.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Movimiento;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;

import com.google.inject.Provider;

@Path("/movimientos")
@Produces(MediaType.APPLICATION_JSON)
public class MovimientosResource 
{
	private static final Logger log = Logger.getLogger(MovimientosResource.class);
	
	@Inject
	private Provider<IService> svcProvider;
	
	@GET
	@Path("/{username}/{fromDate}/{toDate}/{fromRow}/{toRow}")
	public Response get(@PathParam("username") String username,
						@PathParam("fromDate") String fromDate, 
						@PathParam("toDate") String toDate, 
						@PathParam("fromRow") int fromRow, 
						@PathParam("toRow") int toRow)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try 
		{
			Date from = sdf.parse(fromDate);
			Date to = sdf.parse(toDate);
			
			Usuario usuario = svcProvider.get().findUsuarioByUsername(username);
			List<Movimiento> movimientos = svcProvider.get().getMovimientos(usuario, from, to, fromRow, toRow);
			
			return Response.status(Status.OK).entity(movimientos).build();
		} 
		catch (ParseException e) 
		{
			log.error("Problema parseando fecha", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}
	
	@POST
	@Path("/modificar")
	public Response modificar(@FormParam("id") Long id,
							@FormParam("concepto_id") Long conceptoId, 
							@FormParam("fecha") String fecha, 
							@FormParam("detalle") String detalle, 
							@FormParam("importe") Float importe)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Movimiento mov = svcProvider.get().find(Movimiento.class, id);
		Concepto con = svcProvider.get().find(Concepto.class, conceptoId);
		mov.setDetalle(detalle);
		mov.setConcepto(con);
		mov.setImporte(importe);
		
		try 
		{
			mov.setFecha(sdf.parse(fecha));
		} 
		catch (ParseException e) 
		{
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
		
		svcProvider.get().merge(mov);
		
		return Response.status(Status.OK).build();
	}
	
	@POST
	@Path("/borrar")
	public Response borrar(@FormParam("id") Long id)
	{
		Movimiento mov = svcProvider.get().find(Movimiento.class, id);
		svcProvider.get().remove(mov);
		
		return Response.status(Status.OK).build();
	}
	
	@POST
	@Path("/nuevo")
	public Response nuevo(@FormParam("concepto_id") Long conceptoId, 
							@FormParam("fecha") String fecha, 
							@FormParam("detalle") String detalle, 
							@FormParam("importe") Float importe)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Concepto concepto = svcProvider.get().find(Concepto.class, conceptoId);
		
		Movimiento mov = new Movimiento();
		mov.setComunidad(concepto.getComunidad());
		mov.setConcepto(concepto);
		mov.setDetalle(detalle);
		mov.setImporte(importe);
		
		try 
		{
			mov.setFecha(sdf.parse(fecha));
		} 
		catch (ParseException e) 
		{
			log.error("Error al convertir la fecha recibida ["+ fecha +"]", e);
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
		
		svcProvider.get().merge(mov);
		
		return Response.status(Status.OK).build();
	}

}

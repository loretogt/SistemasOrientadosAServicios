package clase.recursos.bbdd;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.naming.NamingContext;

import clase.datos.*;
import clase.recursos.bbdd.*;

@Path("/")
public class Recursos {


	@Context
	private UriInfo uriInfo;

	public Recursos() {}

	@GET
	@Path("/usuarios/{id_usuario}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response verUsuario(@PathParam("id_usuario") String id) {
		Usuario u;
		Backend b = new Backend();
		int usuario_id = Integer.parseInt(id);
		try {
			u = b.verUsuario(usuario_id);
			Url urlUsu = new Url(uriInfo.getAbsolutePath().toString());
			u.setUrl(urlUsu);
			return Response.status(Response.Status.OK).entity(u).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 Internal Server Error").build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario no existe").build();
		}
	}

	@POST
	@Path("/usuarios")
	@Consumes(MediaType.APPLICATION_XML)
	public Response crearUsuario (Usuario u) {
		Backend b = new Backend();
		Usuario n;
		try {
			n = b.crearUsuario(u.getNombre(), u.getApellido1(), u.getApellido2(), u.getEmail());
			String location = uriInfo.getAbsolutePath() + "/" + n.getId();
			return Response.status(Response.Status.CREATED).entity(n).header("Location", location).header("Content-Location", location).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear el usuario\n" + e.getStackTrace()).build();
		} catch (NotAcceptableException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Ya se encuentra el usuario registrado").build();
		} 
	}

	@DELETE
	@Path("/usuarios/{id_usuario}")
	public Response borrarUsuario (@PathParam("id_usuario") String id) {
		Backend b = new Backend();
		int int_id = Integer.parseInt(id);
		try {
			boolean borrado = b.borrarUsuario(int_id);			
			return Response.status(Response.Status.NO_CONTENT).build();	
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo eliminar" + e.getStackTrace()).build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario no existe").build();
		} catch (NotAcceptableException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("El usuario tiene cuentas asociadas").build();
		}
	}


	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Path("usuarios/{id_usuario}")
	public Response editarUsuario(@PathParam("id_usuario") String id, Usuario u) {
		Backend b = new Backend();
		int int_id = Integer.parseInt(id);
		Usuario editado;
		try {
			editado = b.editarUsuario(u, int_id);
			String location = uriInfo.getBaseUri() + "usuarios/" + editado.getId();
			return Response.status(Response.Status.OK).entity(editado).header("Content-Location", location).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 Internal Server Error").build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe dicho usuario").build();
		}
	}

	@POST
	@Path("/cuentas")
	@Consumes(MediaType.APPLICATION_XML)
	public Response crearCuenta (Cuenta c) {
		Backend b = new Backend();
		Cuenta nueva;
		try {
			nueva = b.crearCuenta(c.getSaldo(), c.getIdu());
			String location = uriInfo.getAbsolutePath() + "/" + nueva.getId();
			return Response.status(Response.Status.CREATED).entity(nueva).header("Location", location).header("Content-Location", location).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear la cuenta\n" + e.getStackTrace()).build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario no existe").build();
		}
	}

	@DELETE
	@Path("/cuentas/{id_cuenta}")
	public Response borrarCuenta (@PathParam("id_cuenta") String id) {
		Backend b = new Backend();
		int int_id = Integer.parseInt(id);
		try {
			boolean borrado = b.borrarCuenta(int_id);			
			return Response.status(Response.Status.NO_CONTENT).build();	
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo eliminar" + e.getStackTrace()).build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("La cuenta no existe").build();
		} catch (NotAcceptableException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("La cuenta no tiene saldo 0").build();
		}
	}

	@POST
	@Path("/transferencias")
	@Consumes(MediaType.APPLICATION_XML)
	public Response crearTransferencia (Transferencia t) {
		Backend b = new Backend();
		Transferencia nueva;
		try {
			nueva = b.crearTransferencia(t.getIdo(), t.getIdd(), t.getFecha(), t.getCantidad());
			String location = uriInfo.getAbsolutePath() + "/" + nueva.getId();
			return Response.status(Response.Status.CREATED).entity(nueva).header("Location", location).header("Content-Location", location).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear la transferencia\n" + e.getStackTrace()).build();
		} catch (NotAcceptableException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("La transferencia no se puede realizar: compruebe que las cuentas existan y tengan saldo suficiente").build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("La transferencia no se puede realizar: compruebe que las cuentas existan").build();
		}
	}


	@GET
	@Path("/cuentas/{id_cuenta}/transferencias/{id_transferencia}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response verTransferencia(@PathParam("id_transferencia") String id,
			@PathParam("id_cuenta") String id2) {
		Transferencia u;
		Backend b = new Backend();
		int trans_id = Integer.parseInt(id);
		try {
			u = b.verTransferencia(trans_id);
			Url urlT = new Url(uriInfo.getAbsolutePath().toString());
			u.setUrl(urlT);
			if (u.equals(null)) {
				return Response.status(Response.Status.NOT_FOUND).entity("404 Not found").build(); }
			else {
				return Response.status(Response.Status.OK).entity(u).build();
			}
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 Internal Server Error").build();
		} 
	}


	@DELETE
	@Path("/transferencias/{id_transferencia}")
	public Response borrarTransferencia (@PathParam("id_transferencia") String id) {
		Backend b = new Backend();
		int int_id = Integer.parseInt(id);
		try {
			boolean borrado = b.borrarTransferencia(int_id);			
			return Response.status(Response.Status.NO_CONTENT).build();	
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo eliminar" + e.getStackTrace()).build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe dicha transferencia").build();
		}
	}

	@POST
	@Path("/retiradas")
	@Consumes(MediaType.APPLICATION_XML)
	public Response crearRetirada (Retirada r) {
		Backend b = new Backend();
		Retirada nueva;
		try {
			nueva = b.crearRetirada(r.getCuentaO(), r.getCantidad(), r.getFecha());
			String location = uriInfo.getAbsolutePath() + "/" + nueva.getId();
			return Response.status(Response.Status.CREATED).entity(nueva).header("Location", location).header("Content-Location", location).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear la cuenta\n" + e.getStackTrace()).build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("La retirada no se puede realizar: compruebe que la cuenta exista").build();
		} catch (NotAcceptableException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("La retirada no se puede realizar: compruebe que la cuenta tenga saldo suficient").build();
		}
	}

	@GET
	@Path("/usuarios/{id_usuario}/retiradas/{id_retirada}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response verRetirada(@PathParam("id_retirada") String id,
			@PathParam("id_usuario") String id2) {
		Retirada u;
		Backend b = new Backend();
		int ret_id = Integer.parseInt(id);
		try {
			u = b.verRetirada(ret_id);
			Url urlT = new Url(uriInfo.getAbsolutePath().toString());
			u.setUrl(urlT);
			if (u.equals(null)) {
				return Response.status(Response.Status.NOT_FOUND).entity("404 Not found").build(); }
			else {
				return Response.status(Response.Status.OK).entity(u).build();
			}
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 Internal Server Error").build();
		} 
	}


	@GET
	@Path("/cuentas/{id_cuenta}/transferencias")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTransferencias(@PathParam("id_cuenta") String id,
			@QueryParam("start") @DefaultValue ("0") String start , 
			@QueryParam("end") @DefaultValue ("100")String end, 
			@QueryParam("date") @DefaultValue ("") String date) {
		Backend b = new Backend();
		ArrayList <Transferencia> listado = new ArrayList <Transferencia>();
		int usuario_id = Integer.parseInt(id);
		int st = Integer.parseInt(start);
		int ed = Integer.parseInt(end);
		try {
			listado=b.listarTransferencias(usuario_id,st,ed,date);
			return Response.status(Response.Status.OK).entity(listado).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("La cuenta no existe").build(); 
		}
	}

	@GET
	@Path("/usuarios/{id_usuario}/retiradas")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRetiradas(@PathParam("id_usuario") String id,
			@QueryParam("date") @DefaultValue ("")String date, 
			@QueryParam("cash") @DefaultValue ("") String cash ) {
		Backend b = new Backend();
		ArrayList <Retirada> listado = new ArrayList <Retirada>();
		int usuario_id = Integer.parseInt(id);
		try {
			listado=b.listarRetiradas(usuario_id, date, cash);
			return Response.status(Response.Status.OK).entity(listado).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario no existe").build(); 
		}
	}

	@GET
	@Path("/usuarios")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUsuarios(@QueryParam("start") @DefaultValue ("0") String start , 
			@QueryParam("end") @DefaultValue ("100")String end, 
			@QueryParam("cash") @DefaultValue ("") String cash) {
		Backend b = new Backend();
		ArrayList<Usuario> listado = new ArrayList<Usuario>(); 
		int st = Integer.parseInt(start);
		int ed = Integer.parseInt(end);
		try {
			listado = b.listarUsuarios(st,ed,cash);
			return Response.status(Response.Status.OK).entity(listado).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		}
	}

	@GET
	@Path("/usuarios/{id_usuario}/movimientos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMovimientos(@PathParam("id_usuario") String id,
			@QueryParam("cash") @DefaultValue ("")String cash, 
			@QueryParam("date") @DefaultValue ("") String date) {
		Backend b = new Backend();
		Movimientos listado = new Movimientos();
		int usuario_id = Integer.parseInt(id);
		try {
			listado = b.listarMovimientos(usuario_id, cash, date);
			return Response.status(Response.Status.OK).entity(listado).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario no existe").build(); 
		}
	}

	@GET
	@Path("/usuarios/{id_usuario}/info")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getInfo(@PathParam("id_usuario") String id) {
		Backend b = new Backend();
		InfoMovil info = new InfoMovil();
		int usuario_id = Integer.parseInt(id);
		try {
			info = b.obtenerInfo(usuario_id);
			return Response.status(Response.Status.OK).entity(info).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario no existe").build(); 
		}
	} 
}




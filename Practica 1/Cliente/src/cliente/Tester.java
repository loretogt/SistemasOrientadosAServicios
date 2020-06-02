package cliente;

import java.math.BigDecimal;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import datos.*;

public class Tester {
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/Banco/api").build();
	}


	public static void main(String[] args) {
		 ClientConfig config = new ClientConfig();
		 Client client = ClientBuilder.newClient(config);
		 WebTarget target = client.target(getBaseURI());
		 
		 //Ver usuario existente -> 200 OK 
		 System.out.println("\n Ver usuario existente -> 200");
		 Response r = target.path("usuarios/5").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		 System.out.println("Estado: " + r.getStatus());
		 String valor = r.readEntity(String.class);
		 System.out.println("Entidad: " + valor);
		 
		 //Ver usuario que no exista -> 404 
		 System.out.println("\n Ver usuario que no exista -> 404");
		 r = target.path("usuarios/2").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		 System.out.println("Estado: " + r.getStatus());
		 valor = r.readEntity(String.class);
		 System.out.println("Entidad: " + valor);
		 
		 //Crear usuario -> 201 OK
		 System.out.println("\n Crear usuario -> 201 OK");
		 Usuario u= new Usuario ("jose carlos", "fernandez", "garcia", "josecarlosf@gmail.com");
		 r= target.path("usuarios").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(u),Response.class);
		 System.out.println("Estado: " + r.getStatus() + " \n Entidad: " + r.readEntity(String.class));
		 if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
			}  
		
		 //Crear usuario que ya exista -> 406
		 System.out.println("\n Crear usuario que ya exista -> 406");
		 u= new Usuario ("jose carlos", "fernandez", "garcia", "josecarlosf@gmail.com");
		 r= target.path("usuarios").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(u),Response.class);
		 System.out.println("Estado: " + r.getStatus() + " \n Entidad: " + r.readEntity(String.class));
		 if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}  
		 
		 //Modificar usuario -> 200 OK
		System.out.println("\n Modificar usuario -> 200");
		Usuario u2= new Usuario (6,"loreto", "garcia", "tejasa", "loreto@gmail.com");
		u2.setEmail("loretogarcia@gmail.com");
		u2.setApellido2("tejada");
		r = target.path("usuarios").path(Integer.toString(u2.getId())).request().accept(MediaType.APPLICATION_XML).put(Entity.xml(u2),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		} 
		
		//Modificar usuario que no existe -> 404
		System.out.println("\n Modificar usuario que no existe -> 404");
		u2= new Usuario (2,"loreto", "garcia", "tejasa", "loreto@gmail.com");
		u2.setEmail("loretogarcia@gmail.com");
		u2.setApellido2("tejada");
		r = target.path("usuarios").path(Integer.toString(u2.getId())).request().accept(MediaType.APPLICATION_XML).put(Entity.xml(u2),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		} 
		
		
		//Borrar usuario -> 204
		System.out.println("\n Borrar usuario -> 204");
		r = target.path("usuarios/15").request().accept(MediaType.APPLICATION_JSON).delete(Response.class);
		System.out.println("Estado: " + r.getStatus());
		
		
		//Borrar usuario que no existe -> 404 
		System.out.println("\n Borrar usuario que no existe -> 404");
		r = target.path("usuarios/3").request().accept(MediaType.APPLICATION_JSON).delete(Response.class);
		System.out.println("Estado: " + r.getStatus());
		
		//Borrar usuario que tenga cuentas abiertas -> 406
		System.out.println("\n Borrar usuario que tenga cuentas abiertas -> 406");
		r = target.path("usuarios/16").request().accept(MediaType.APPLICATION_JSON).delete(Response.class);
		System.out.println("Estado: " + r.getStatus());
		
		
		//Crear cuenta -> 201 OK 
		System.out.println("\n Crear cuenta -> 201");
		BigDecimal saldo = new BigDecimal("50");
		Cuenta c = new Cuenta(saldo,12);
		r= target.path("cuentas").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(c),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
			} 
		
		//Crear cuenta a usuario no existente -> 404
		System.out.println("\n Crear cuenta a usuario no existente -> 404");
		saldo = new BigDecimal("100");
		c = new Cuenta(saldo,11);
		r= target.path("cuentas").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(c),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
			} 
		
		//Borrar cuenta -> 204
		System.out.println("\n Borrar cuenta -> 204");
		r = target.path("cuentas/27").request().accept(MediaType.APPLICATION_JSON).delete(Response.class);
		System.out.println("Estado: " + r.getStatus());
		
		//Borrar cuenta con saldo distinto de 0 -> 406
		System.out.println("\n Borrar cuenta con saldo distinto de 0 -> 406");
		r = target.path("cuentas/22").request().accept(MediaType.APPLICATION_JSON).delete(Response.class);
		System.out.println("Estado: " + r.getStatus());
		
		//Borrar cuenta con cuenta no existente -> 404
		System.out.println("\n Borrar cuenta con cuenta inexistente -> 404");
		r = target.path("cuentas/43").request().accept(MediaType.APPLICATION_JSON).delete(Response.class);
		System.out.println("Estado: " + r.getStatus());
		
		
		//Crear transferencia -> 201 OK
		System.out.println("\n Crear transferencia -> 201 OK");
		Transferencia t = new Transferencia(22,30,"2020-04-14",saldo);
		r= target.path("transferencias").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(t),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
			}  
		
		//Crear transferencia con cuenta origen no existente-> 404
		System.out.println("\n Crear transferencia con cuenta origen no existente -> 404");
		t = new Transferencia(38,30,"2020-04-14",saldo);
		r= target.path("transferencias").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(t),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}  
		
		//Crear transferencia con saldo insuficiente -> 406
		System.out.println("\n Crear transferencia con saldo insuficiente -> 406");
		t = new Transferencia(21,30,"2020-04-14",saldo);
		r= target.path("transferencias").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(t),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}  
		
		
		//Eliminar transferencias -> 204
		System.out.println("\n Eliminar transferencias -> 204");
		r = target.path("transferencias/10").request().accept(MediaType.APPLICATION_JSON).delete(Response.class);
		System.out.println("Estado: " + r.getStatus());
		
		//Eliminar transferencias cuando no existe -> 404
		System.out.println("\n Eliminar transferencias cuando no existe -> 404");
		r = target.path("transferencias/10").request().accept(MediaType.APPLICATION_JSON).delete(Response.class);
		System.out.println("Estado: " + r.getStatus());
		
		
		//Listar transferencias -> 200
		System.out.println("\n Listar transferencias -> 200");
		r = target.path("cuentas/28/transferencias").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
		//Listar transferencias -> 404
		System.out.println("\n Listar transferencias cuando no existe cuenta -> 404");
		r = target.path("cuentas/1/transferencias").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
		//Crear retirada -> 201
		System.out.println("\n Crear retirada -> 201 OK");
		saldo = new BigDecimal("20");
		Retirada ret = new Retirada(19,saldo,"2020-03-23");
		r= target.path("retiradas").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(ret),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}  
		
		//Crear retirada -> 404
		System.out.println("\n Crear retirada con cuenta inexistente -> 404");
		saldo = new BigDecimal("20");
		ret = new Retirada(29,saldo,"2020-03-23");
		r= target.path("retiradas").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(ret),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}  
		
		//Crear retirada -> 406
		System.out.println("\n Crear retirada con saldo insuficiente -> 406");
		saldo = new BigDecimal("200");
		ret = new Retirada(20,saldo,"2020-03-23");
		r= target.path("retiradas").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(ret),Response.class);
		System.out.println("Estado: " + r.getStatus() + "\n Entidad: " + r.readEntity(String.class));
		if(r.getHeaders().containsKey("Location")) {
			Object location = r.getHeaders().get("Location").get(0);
			System.out.println("Location: " + location.toString());
		}  
		
		//Listar retiradas -> 200
		System.out.println("\n Listar retiradas -> 200 OK");
		r = target.path("usuarios/6/retiradas").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
		
		//Listar retiradas -> 404
		System.out.println("\n Listar retiradas cuando usuario inexistente -> 404");
		r = target.path("usuarios/2/retiradas").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
		//Listar movimientos -> 200
		System.out.println("\n Listar movimientos -> 200");
		r = target.path("usuarios/6/movimientos").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
		//Listar movimientos -> 404
		System.out.println("\n Listar movimientos cuando usuario inexistente -> 404");
		r = target.path("usuarios/2/movimientos").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
		
	
		//Listar usuarios -> 200 Ok
		System.out.println("\n Listar usuarios -> 200 OK");
		r = target.path("usuarios").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
		//Listar info -> 200
		System.out.println("\n Listar info -> 200 OK");
		r = target.path("usuarios/6/info").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
		//Listar info -> 404
		System.out.println("\n Listar info -> 404");
		r = target.path("usuarios/2/info").request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		System.out.println("Estado: " + r.getStatus());
		valor = r.readEntity(String.class);
		System.out.println("Entidad: " + valor);
		
	}

}

package clase.datos;



import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Link;

import org.apache.naming.NamingContext;

import clase.datos.*;


public class Backend {
	private DataSource ds;
	private Connection conn;
	
	public Backend () {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			NamingContext envCtx = (NamingContext) ctx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/Banco");
			conn = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Usuario crearUsuario (String nombre, String apellido1, String apellido2, String email) throws SQLException {
		String comp = "SELECT * FROM usuarios WHERE email='" + email + "';";
		PreparedStatement st = conn.prepareStatement(comp);
		ResultSet rs2 = st.executeQuery();
		Usuario usu = null;
		if (rs2.next()) {
			throw new NotAcceptableException();
		}
		String sql = "INSERT INTO usuarios (nombre, apellido1, apellido2, email) VALUES (?,?,?,?);";
		PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, nombre);
		ps.setString(2, apellido1);
		ps.setString(3, apellido2);
		ps.setString(4, email);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if(rs.next()) {
			usu = new Usuario (rs.getInt(1), nombre, apellido1, apellido2, email);
		}
		return usu;
	}
	
	public Usuario verUsuario (int id) throws SQLException {
		Usuario usu = null;
		Url url = null;
		PreparedStatement st = conn.prepareStatement("SELECT * FROM usuarios WHERE idusuarios='" + id + "';");
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			usu = new Usuario(rs.getInt("idusuarios"),rs.getString("nombre"),rs.getString("apellido1"),
					rs.getString("apellido2"), rs.getString("email"), url);
		}
		else{
			throw new NotFoundException();
		}
		return usu;
	}
	
	public boolean borrarUsuario (int id) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM usuarios WHERE idusuarios='" + id + "';" );
		if (!rs.next()) {
			throw new NotFoundException();
		}
		Statement st1 = conn.createStatement();
		ResultSet rs1 = st1.executeQuery("SELECT * FROM cuentas WHERE idusuario='" + id + "';" );
		if (rs1.next()) {
			throw new NotAcceptableException();
		}
		PreparedStatement ps = conn.prepareStatement("DELETE FROM usuarios WHERE idusuarios=" + id + ";");
		return ps.executeUpdate()==1;
	}
	
	public Usuario editarUsuario (Usuario usu, int id) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM usuarios WHERE idusuarios='" + id + "';" );
		if (!rs.next()) {
			throw new NotFoundException();
		}
		Usuario antiguo = new Usuario(id, rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
		antiguo.setNombre(usu.getNombre());
		antiguo.setApellido1(usu.getApellido1());
		antiguo.setApellido2(usu.getApellido2());
		antiguo.setEmail(usu.getEmail());
		PreparedStatement ps = conn.prepareStatement("UPDATE usuarios SET nombre='"+ antiguo.getNombre() +
				"', apellido1='" + antiguo.getApellido1() + "', apellido2='" + antiguo.getApellido2() + 
				"', email='" + antiguo.getEmail() + "' WHERE idusuarios='" + id + "';");
		ps.executeUpdate();
		antiguo.setId(id);
		return antiguo;	
	}
	
	public Cuenta crearCuenta (BigDecimal saldo, int usu) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM usuarios WHERE idusuarios='"+ usu + "';");
		if (!rs.next()) {
			throw new NotFoundException();
		}
		Cuenta nueva = null;
		String sql = "INSERT INTO cuentas (idusuario, saldo) VALUES (?,?);";
		PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, usu);
		ps.setBigDecimal(2, saldo);
		ps.executeUpdate();
		ResultSet rs2 = ps.getGeneratedKeys();
		if(rs2.next()) {
			nueva = new Cuenta(rs2.getInt(1), saldo, usu);
		}
		return nueva;	
	}
	
	public boolean borrarCuenta (int id) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM cuentas WHERE idcuentas='" + id + "';" );
		if (!rs.next()) {
			throw new NotFoundException();
		}
		BigDecimal saldo = rs.getBigDecimal(3);
		BigDecimal saldo0 = new BigDecimal("0.00");
		if (!saldo.equals(saldo0)) {
			throw new NotAcceptableException();
		}
		PreparedStatement ps = conn.prepareStatement("DELETE  FROM cuentas WHERE idcuentas='" + id + "';");
		return ps.executeUpdate()==1;
	}
	
	public Transferencia crearTransferencia (int o, int d, String fecha, BigDecimal cantidad) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM cuentas WHERE idcuentas='"+ o + "';"); //Compruebo que la cuenta origen existe
		if (!rs.next()) {
			throw new NotFoundException();
		}
		//Compruebo que hay saldo suficiente para ejecutar la transaccion
		BigDecimal saldoactual = rs.getBigDecimal(3);
		if(saldoactual.compareTo(cantidad)==-1) {
			throw new NotAcceptableException();
		}
		rs = st.executeQuery("SELECT * FROM cuentas WHERE idcuentas='"+ d + "';"); //Compruebo que la cuenta destino existe 
		if (!rs.next()) {
			throw new NotFoundException();
		}
		PreparedStatement ps = conn.prepareStatement("UPDATE cuentas SET saldo=saldo -'"+ cantidad + " 'WHERE idcuentas='" + o + "';");
		if (ps.executeUpdate() != 1) {
			throw new NotAcceptableException();
		}
		//Creo la transferencia
		Transferencia nueva = null;
		String sql = "INSERT INTO transferencias (cuentaorigen, cuentadestino, fecha, cantidad) VALUES (?,?,?,?);";
		PreparedStatement ps2 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps2.setInt(1, o);
		ps2.setInt(2, d);
		ps2.setString(3, fecha);
		ps2.setBigDecimal(4, cantidad);
		ps2.executeUpdate();
		ResultSet rs2 = ps2.getGeneratedKeys();
		if(rs2.next()) {
			nueva = new Transferencia(rs2.getInt(1),o,d,fecha,cantidad);
		}
		PreparedStatement ps3 = conn.prepareStatement("UPDATE cuentas SET saldo=saldo +'"+ cantidad + " 'WHERE idcuentas='" + d + "';");
		ps3.executeUpdate();
		return nueva;	
	}
	
	public Transferencia verTransferencia (int id) throws SQLException {
		Transferencia ut = null;
		Url url = null;
		PreparedStatement st = conn.prepareStatement("SELECT * FROM transferencias WHERE idtransferencias='" + id + "';");
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			ut = new Transferencia(rs.getInt(1),rs.getInt(2),rs.getInt(3),
					rs.getString(4), rs.getBigDecimal(5), url);
		}
		else{
			throw new NotFoundException();
		}
		return ut;
	}
	
	public boolean borrarTransferencia (int id) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM transferencias WHERE idtransferencias='" + id + "';" );
		if (!rs.next()) {
			throw new NotFoundException();
		}
		int idorigen = rs.getInt(2);
		int iddestino = rs.getInt(3);
		BigDecimal cantidad = rs.getBigDecimal(5);
		PreparedStatement ps = conn.prepareStatement("DELETE FROM transferencias WHERE idtransferencias='" + id + "';");
		PreparedStatement ps2 = conn.prepareStatement("UPDATE cuentas SET saldo=saldo +'"+ cantidad + " 'WHERE idcuentas='" + idorigen + "';");
		ps2.executeUpdate();
		PreparedStatement ps3 = conn.prepareStatement("UPDATE cuentas SET saldo=saldo -'"+ cantidad + " 'WHERE idcuentas='" + iddestino + "';");
		ps3.executeUpdate();
		
		return ps.execute();
	}
	
	public Retirada crearRetirada (int o, BigDecimal cantidad, String fecha) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM cuentas WHERE idcuentas='"+ o + "';"); //Compruebo que la cuenta origen existe
		if (!rs.next()) {
			throw new NotFoundException();
		}
		BigDecimal saldoactual = rs.getBigDecimal(3);
		if(saldoactual.compareTo(cantidad)==-1) {
			throw new NotAcceptableException();
		}
		PreparedStatement ps = conn.prepareStatement("UPDATE cuentas SET saldo=saldo -'"+ cantidad + " 'WHERE idcuentas='" + o + "';");
		//Compruebo que hay saldo suficiente para ejecutar la transaccion
		if (ps.executeUpdate() != 1) {
			throw new NotAcceptableException();
		}
		//Creo la retirada
		Retirada nueva = null;
		String sql = "INSERT INTO retiradas (cuenta, cantidad, fecha) VALUES (?,?,?);";
		PreparedStatement ps2 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps2.setInt(1, o);
		ps2.setBigDecimal(2, cantidad);
		ps2.setString(3, fecha);
		ps2.executeUpdate();
		ResultSet rs2 = ps2.getGeneratedKeys();
		if(rs2.next()) {
				nueva = new Retirada (rs2.getInt(1),o,cantidad, fecha);
		}
		return nueva;
	}
	
	public Retirada verRetirada (int id) throws SQLException {
		Retirada r = null;
		Url url = null;
		PreparedStatement st = conn.prepareStatement("SELECT * FROM retiradas WHERE idretiradas='" + id + "';");
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			r = new Retirada(rs.getInt(1),rs.getInt(2), rs.getBigDecimal(3),rs.getString(4), url);
		}
		else{
			throw new NotFoundException();
		}
		return r;
	}
	

	public ArrayList<Transferencia> listarTransferencias (int id, int start, int end, String fecha) throws SQLException{
		ArrayList <Transferencia> listado = new ArrayList <Transferencia>();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM cuentas WHERE idcuentas="+ id + ";");
		if(!rs.next()) {
			throw new NotFoundException();
		}
		String url;
		String filtrado = "";
		if(!fecha.isEmpty()) {
			filtrado = "AND fecha='" +fecha +"' ";
		}
		Statement st2 = conn.createStatement();
		ResultSet rs2 = st2.executeQuery("SELECT * FROM transferencias WHERE cuentaorigen='"+ id + "'" + filtrado +
				"LIMIT " + start + "," + end + " ;");
		while (rs2.next()) {
			String cuenta = Integer.toString(id);
			url = "http://localhost:8080/Banco/api/cuentas/" + cuenta + "/transferencias/" + rs2.getString(1);
			Url urlaux = new Url(url);
			Transferencia t = new Transferencia (rs2.getInt(1),id, rs2.getInt(3),rs2.getString(4),rs2.getBigDecimal(5),urlaux);
			listado.add(t);
		}
		return listado;	
	}
	
	public ArrayList<Retirada> listarRetiradas (int id, String fecha, String cantidad) throws SQLException{
		String url;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * from usuarios WHERE idusuarios='"+ id + "';");
		if(!rs.next()) {
			throw new NotFoundException();
		}
		
		String filtrado="";
		String filcan ="";
		
		if(!fecha.isEmpty()) {
			filtrado = "AND fecha='" +fecha +"' ";
		}
		if(!cantidad.isEmpty()) {
			filcan = "AND cantidad='" + cantidad + "' ";
		}

		Statement st1 = conn.createStatement();
		ResultSet rs1 = st1.executeQuery("SELECT * FROM retiradas join cuentas ON (retiradas.cuenta=cuentas.idcuentas) WHERE "
				+ "cuentas.idusuario='" + id + "' "+ filtrado + filcan + ";");
		
		ArrayList <Retirada> listado = new ArrayList<Retirada>();
		while(rs1.next()) {
			String usuario = Integer.toString(id);
			url = "http://localhost:8080/Banco/api/usuarios/" + usuario + "/retiradas/" + rs1.getString(1);
			Url urlaux = new Url(url);
			Retirada r = new Retirada(rs1.getInt(1), rs1.getInt(2), rs1.getBigDecimal(3), rs1.getString(4), urlaux);
			listado.add(r);
		}
		return listado;
		
	}
	
	public ArrayList<Usuario>  listarUsuarios (int start, int end, String dinero) throws SQLException{
		ArrayList <Usuario> listado = new ArrayList<Usuario>();
		String url;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT idusuario, SUM(saldo) FROM usuarios join cuentas ON (cuentas.idusuario = usuarios.idusuarios) GROUP BY idusuario "
				+ "LIMIT " + start + "," + end + "; ");
	
		while(rs.next()) {
			if(dinero.isEmpty()) {
				url = "http://localhost:8080/Banco/api/usuarios/" + rs.getString(1);
				Url urlaux = new Url(url);
				Usuario u = new Usuario(rs.getInt(1), rs.getBigDecimal(2), urlaux);
				listado.add(u);
			}
			if(dinero.equals(rs.getString(2))) {
				url = "http://localhost:8080/Banco/api/usuarios/" + rs.getString(1);
				Url urlaux = new Url(url);
				Usuario u = new Usuario(rs.getInt(1), rs.getBigDecimal(2), urlaux);
				listado.add(u);
			}
		}
		return listado;	
	}
	
	public Movimientos listarMovimientos (int id, String cantidad, String fecha) throws SQLException{
		Movimientos mov = new Movimientos();
		ArrayList<Retirada> retiradas = new ArrayList<Retirada>();
		ArrayList<Transferencia> transferencias = new ArrayList<Transferencia>();
		String filtrado="";
		String filcan="";
		if(!fecha.isEmpty()) {
			filtrado = "AND fecha='" +fecha +"' ";
		}
		if(!cantidad.isEmpty()) {
			filcan = "AND cantidad='" + cantidad + "' ";
		}
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * from usuarios WHERE idusuarios='"+ id + "';");
		if(!rs.next()) {
			throw new NotFoundException();
		}
		Statement st1 = conn.createStatement();
		ResultSet rs1 = st1.executeQuery("SELECT * FROM transferencias join cuentas ON (transferencias.cuentaorigen=cuentas.idcuentas) WHERE "
				+ "cuentas.idusuario='" + id + "' "+ filtrado + filcan + ";");
		while (rs1.next()) {
			String cuenta = Integer.toString(id);
			String url = "http://localhost:8080/Banco/api/cuentas/" + cuenta + "/transferencias/" + rs1.getString(1);
			Url urlaux = new Url(url);
			Transferencia t = new Transferencia (rs1.getInt(1),rs1.getInt(2), rs1.getInt(3),rs1.getString(4),rs1.getBigDecimal(5),urlaux);
			transferencias.add(t);
		}
		Statement st2 = conn.createStatement();
		ResultSet rs2 = st2.executeQuery("SELECT * FROM retiradas join cuentas ON (retiradas.cuenta=cuentas.idcuentas) WHERE "
				+ "cuentas.idusuario='" + id + "' "+ filtrado  + filcan + ";");
		while(rs2.next()) {
			String usuario = Integer.toString(id);
			String url = "http://localhost:8080/Banco/api/usuarios/" + usuario + "/retiradas/" + rs2.getString(1);
			Url urlaux = new Url(url);
			Retirada r = new Retirada(rs2.getInt(1), rs2.getInt(2), rs2.getBigDecimal(3), rs2.getString(4), urlaux);
			retiradas.add(r);
		}
		mov.setRetiradas(retiradas);
		mov.setTransferencias(transferencias);
		return mov;
	}
	
	public InfoMovil obtenerInfo (int id) throws SQLException {
		InfoMovil info = new InfoMovil();
		Usuario usu = verUsuario(id);
		info.setUsuario(usu);
		ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM cuentas WHERE idusuario='"+ id + "';");
		while (rs.next()) {
			Cuenta nueva = new Cuenta(rs.getInt(1), rs.getBigDecimal(3), rs.getInt(2));
			cuentas.add(nueva);
		}
		info.setUsuario(usu);
		info.setCuentas(cuentas);
		ArrayList<Url> movimientos = new ArrayList<Url>();
		String sql = "SELECT idretiradas AS id, fecha, 1 FROM retiradas INNER JOIN cuentas ON (cuentas.idcuentas = retiradas.cuenta) WHERE cuentas.idusuario='" + id + "' UNION ALL SELECT idtransferencias AS id, fecha, 2 FROM transferencias INNER JOIN cuentas ON (cuentas.idcuentas = transferencias.cuentaorigen) WHERE cuentas.idusuario='" + id + "' ORDER BY fecha DESC LIMIT 10 ;";
		PreparedStatement st1 = conn.prepareStatement(sql);
		ResultSet rs1 = st1.executeQuery();
		while (rs1.next()) {
			if(rs1.getInt(3)==2) {
				String cuenta = Integer.toString(id);
				String url = "http://localhost:8080/Banco/api/cuentas/" + cuenta + "/transferencias/" + rs1.getString(1);
				String rel = "self";
				Url urlaux = new Url(url,rel);
				movimientos.add(urlaux);
			}
			else {
				String usuario = Integer.toString(id);
				String url = "http://localhost:8080/Banco/api/usuarios/" + usuario + "/retiradas/" + rs1.getString(1);
				String rel = "self";
				Url urlaux = new Url(url,rel);
				movimientos.add(urlaux);
			}
		}
		
		info.setMovimientos(movimientos);
		return info;
	}
	
	
}

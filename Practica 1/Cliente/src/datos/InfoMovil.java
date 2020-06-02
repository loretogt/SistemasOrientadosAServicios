package datos;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="info")
public class InfoMovil {

	private Usuario usuario;
	private ArrayList<Cuenta> cuentas;
	private ArrayList<Url> movimientos;
	
	public InfoMovil (Usuario u, ArrayList<Cuenta> cuentas, ArrayList<Url> movimientos) {
		this.usuario=u;
		this.cuentas=cuentas;
		this.movimientos=movimientos;
	}

	public ArrayList<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(ArrayList<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public InfoMovil() {
	
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	

	public ArrayList<Url> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(ArrayList<Url> movimientos) {
		this.movimientos = movimientos;
	}
}

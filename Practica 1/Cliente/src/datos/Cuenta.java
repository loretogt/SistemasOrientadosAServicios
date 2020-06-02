package datos;


import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="cuenta")
public class Cuenta {
	
	private int id;
	private BigDecimal saldo;
	private Usuario usuario;
	private int idu;


	
	public int getIdu() {
		return idu;
	}

	public void setIdu(int idu) {
		this.idu = idu;
	}

	public Cuenta() {
		
	}
	
	public Cuenta (int id, BigDecimal saldo, int idu) {
		this.id=id;
		this.saldo=saldo;
		this.idu=idu;
		//this.usuario.setId(idu);
		
	}
	
	public Cuenta (BigDecimal saldo, int idu) {
		this.saldo=saldo;
		this.idu=idu;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}

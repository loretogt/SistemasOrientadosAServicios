package datos;

import java.math.BigDecimal;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="retirada")
public class Retirada {
	
	private int id;
	private Cuenta cuenta;
	private BigDecimal cantidad;
	private String fecha;
	private Url url;
	private int cuentaO;
	
	public Retirada() {
		
	}
	
	public Retirada (int id, int cuenta, BigDecimal cantidad, String fecha) {
		this.cuentaO=cuenta;
		this.id=id;
		this.cantidad=cantidad;
		this.fecha=fecha;
	}
	
	public Retirada (int cuenta, BigDecimal cantidad, String fecha) {
		this.cuentaO=cuenta;
		this.cantidad=cantidad;
		this.fecha=fecha;
	}
	
	public Retirada (int id, int cuenta, BigDecimal cantidad, String fecha, Url url) {
		this.id=id;
		this.cantidad=cantidad;
		this.fecha=fecha;
		this.url=url;
		this.cuentaO=cuenta;
	}
	
	public int getCuentaO() {
		return cuentaO;
	}

	public void setCuentaO(int cuentaO) {
		this.cuentaO = cuentaO;
	}

	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	} 

	
}

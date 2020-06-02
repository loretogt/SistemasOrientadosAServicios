package clase.datos;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "transferencia")
public class Transferencia {

	private int id;
	private Cuenta origen;
	private Cuenta destino;
	private String fecha;
	private BigDecimal cantidad;
	private Url url;
	private int ido;
	private int idd;
	

	public Transferencia() {
		
	}
	
	public Transferencia (int id, int ido, int idd, String fecha, BigDecimal cantidad) {
		this.id=id;
		this.cantidad=cantidad;
		this.fecha=fecha;
		this.ido=ido;
		this.idd=idd;
	}
	
	public Transferencia (int ido, int idd, String fecha, BigDecimal cantidad) {
		this.cantidad=cantidad;
		this.fecha=fecha;
		this.ido=ido;
		this.idd=idd;
	}
	
	public Transferencia (int id, int ido, int idd, String fecha, BigDecimal cantidad, Url url) {
		this.id=id;
		this.cantidad=cantidad;
		this.fecha=fecha;
		this.url=url;
		this.ido=ido;
		this.idd=idd;
	}

	public int getIdo() {
		return ido;
	}

	public void setIdo(int ido) {
		this.ido = ido;
	}

	public int getIdd() {
		return idd;
	}

	public void setIdd(int idd) {
		this.idd = idd;
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

	public Cuenta getOrigen() {
		return origen;
	}

	public void setOrigen(Cuenta origen) {
		this.origen = origen;
	}

	public Cuenta getDestino() {
		return destino;
	}

	public void setDestino(Cuenta destino) {
		this.destino = destino;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	


	
	
}

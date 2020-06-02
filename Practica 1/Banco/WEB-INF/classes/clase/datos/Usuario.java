package clase.datos;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "usuario")
public class Usuario {
	private int id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String email;
	private Url url;
	private BigDecimal saldo;
	
	public Usuario (int id, String nombre, String apellido1, String apellido2, String email) {
		this.id=id;
		this.nombre=nombre;
		this.apellido1=apellido1;
		this.apellido2=apellido2;
		this.email=email;
	}
	
	public Usuario (String nombre, String apellido1, String apellido2, String email) {
		this.nombre=nombre;
		this.apellido1=apellido1;
		this.apellido2=apellido2;
		this.email=email;
	}
	
	public Usuario (int id, String nombre, String apellido1, String apellido2, String email, Url url) {
		this.id=id;
		this.nombre=nombre;
		this.apellido1=apellido1;
		this.apellido2=apellido2;
		this.email=email;
		this.url=url;
	}
	
	public Usuario (int id, BigDecimal saldo, Url url) {
		this.id=id;
		this.saldo=saldo;
		this.url=url;
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public Usuario () { }

	//@XmlAttribute(required=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}

package datos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="movimientos")
public class Movimientos {

	private ArrayList<Transferencia> transferencias;
	private ArrayList<Retirada> retiradas;
	
	public Movimientos () {
		this.transferencias = new ArrayList<Transferencia>();
		this.retiradas = new ArrayList<Retirada>();
	}
	
	//@XmlElement (name="transferencia")
	public ArrayList<Transferencia> getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(ArrayList<Transferencia> transferencias) {
		this.transferencias = transferencias;
	}
	
	//@XmlElement (name="retirada")
	public ArrayList<Retirada> getRetiradas() {
		return retiradas;
	}

	public void setRetiradas(ArrayList<Retirada> retiradas) {
		this.retiradas = retiradas;
	}
	
	
}

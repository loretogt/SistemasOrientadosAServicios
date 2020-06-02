
/**
 * UPMBankWSSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package es.upm.fi.sos.upmbank;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;

import org.apache.axis2.AxisFault;

import es.upm.fi.sos.upmbank.client.UPMAuthenticationAuthorizationWSSkeletonStub;
import es.upm.fi.sos.upmbank.xsd.AddMovementResponse;
import es.upm.fi.sos.upmbank.xsd.BankAccount;
import es.upm.fi.sos.upmbank.xsd.BankAccountResponse;
import es.upm.fi.sos.upmbank.xsd.MovementList;
import es.upm.fi.sos.upmbank.xsd.Response;
import es.upm.fi.sos.upmbank.xsd.User;
/**
 *  UPMBankWSSkeleton java skeleton for the axisService
 */
public class UPMBankWSSkeleton{

	private User UsuarioActivo = new User();
	private boolean estamosDentro;
	private static ArrayList<User> sesionesActivadas = new ArrayList<User>();
	private static HashMap<String,ArrayList<Pair<BankAccount,Double>>> cuentasDelBanco = new HashMap<String,ArrayList<Pair<BankAccount,Double>>>();
	private static HashMap<String,ArrayList<Pair<String,Double>>> movimientosCuenta = new HashMap<String,ArrayList<Pair<String,Double>>>();
	private UPMAuthenticationAuthorizationWSSkeletonStub upm;
	/**
	 * Auto generated method signature
	 * 
	 * @param addBankAcc 
	 * @return addBankAccResponse 
	 * @throws AxisFault 
	 */

	public UPMBankWSSkeleton() throws AxisFault{
		User admin =new User();
		admin.setName("admin");
		admin.setPwd("admin");
		this.estamosDentro=false;
		this.upm = new UPMAuthenticationAuthorizationWSSkeletonStub();
	}

	public es.upm.fi.sos.upmbank.AddBankAccResponse addBankAcc(es.upm.fi.sos.upmbank.AddBankAcc addBankAcc){
		BankAccountResponse cuenta = new BankAccountResponse();
		cuenta.setResult(false);
		AddBankAccResponse cuentanueva = new AddBankAccResponse();
		cuentanueva.set_return(cuenta);
		//Si el usuario no ha hecho login 
		if(!this.estamosDentro || !this.tengoSesion(UsuarioActivo)){
			return cuentanueva;
		}
		Double cantidad = addBankAcc.getArgs0().getQuantity();
		BankAccount cuentaCliente = new BankAccount();
		String IBAN = "";
		for (int i = 0; i<9;i++){
			int numero = (int)(Math.random()*9+1);
			IBAN = IBAN + numero;
		}
		cuentaCliente.setIBAN("ES23" + IBAN);
		if(this.UsuarioActivo!=null && UPMBankWSSkeleton.getCuentasDelBanco().containsKey(UsuarioActivo.getName())==true){
			UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).add(new Pair<>(cuentaCliente,cantidad));
		}
		else{
			ArrayList<Pair<BankAccount,Double>> cuentas = new ArrayList<Pair<BankAccount,Double>>();
			cuentas.add(new Pair<>(cuentaCliente,cantidad));
			UPMBankWSSkeleton.getCuentasDelBanco().put(UsuarioActivo.getName(),cuentas);
		}
		cuenta.setIBAN("ES23" + IBAN);
		cuenta.setResult(true);
		cuentanueva.set_return(cuenta);
		return cuentanueva;

	}


	/**
	 * Auto generated method signature
	 * 
	 * @param closeBankAcc 
	 * @return closeBankAccResponse 
	 */

	public es.upm.fi.sos.upmbank.CloseBankAccResponse closeBankAcc
	(es.upm.fi.sos.upmbank.CloseBankAcc closeBankAcc)
	{
		CloseBankAccResponse cuentaCerrar = new CloseBankAccResponse();
		Response r = new Response();
		r.setResponse(false);
		cuentaCerrar.set_return(r);
		//Si el usuario no ha hecho login 
		if(!this.estamosDentro || !this.tengoSesion(UsuarioActivo)){
			return cuentaCerrar;
		}
		//Si la cuenta no existe
		if(this.UsuarioActivo!= null && UPMBankWSSkeleton.getCuentasDelBanco().containsKey(UsuarioActivo.getName())!=false){
			for(int i = 0; i< UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).size(); i++){
				if(UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).get(i).getKey().getIBAN().equals(closeBankAcc.localArgs0.getIBAN())){
					if(UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).get(i).getValue().equals(0.00)){
						UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).remove(i);
						r.setResponse(true);
						cuentaCerrar.set_return(r);
						//Borramos los movimientos asociados a dicha cuenta 
						for (int j = 0; j<UPMBankWSSkeleton.getMovimientosCuenta().get(UsuarioActivo.getName()).size(); j++){
							if(UPMBankWSSkeleton.getMovimientosCuenta().get(UsuarioActivo.getName()).get(j).getKey().equals(closeBankAcc.localArgs0.getIBAN())){
								UPMBankWSSkeleton.getMovimientosCuenta().get(UsuarioActivo.getName()).remove(j);
								j--;
							}
						}
					}
				}		
			}
		}
		return cuentaCerrar;
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param logout 
	 * @return  
	 */

	public void logout (es.upm.fi.sos.upmbank.Logout logout){
		if(this.estamosDentro && this.UsuarioActivo!=null){
			UPMBankWSSkeleton.getSesionesActivadas().remove(UsuarioActivo);
			this.estamosDentro=false;
		}
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param removeUser 
	 * @return removeUserResponse 
	 * @throws RemoteException 
	 */

	public es.upm.fi.sos.upmbank.RemoveUserResponse removeUser
	(es.upm.fi.sos.upmbank.RemoveUser removeUser) throws RemoteException{
		RemoveUserResponse borrado = new RemoveUserResponse();
		Response r = new Response();
		r.setResponse(false);
		borrado.set_return(r);
		//Solo puede borrar el admin 
		if(!this.estamosDentro || (this.estamosDentro && this.UsuarioActivo!= null && !this.UsuarioActivo.getName().equals("admin"))
				|| !this.tengoSesion(UsuarioActivo)){
			return borrado;
		}

		UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE quitar = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE();
		UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser quitado = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser();	
		UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserResponse quitadoR = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserResponse();

		//Comprobamos que el usuario no tiene cuentas en el banco
		if(!UPMBankWSSkeleton.getCuentasDelBanco().isEmpty()){
			if(UPMBankWSSkeleton.getCuentasDelBanco().containsKey(removeUser.getArgs0().getUsername())==true){
				if(UPMBankWSSkeleton.getCuentasDelBanco().get(removeUser.getArgs0().getUsername()).isEmpty()==true){
					quitado.setName(removeUser.localArgs0.getUsername());
					quitar.setRemoveUser(quitado);
					quitadoR = upm.removeUser(quitar).get_return();
					if(quitadoR.getResult()==true){
						r.setResponse(true);
						borrado.set_return(r);
					}
				}
			}
		}
		if(UPMBankWSSkeleton.getCuentasDelBanco().isEmpty()){
			r.setResponse(true);
			borrado.set_return(r);
		}
		//Comprobamos que el usuario tiene sesiones activas 
		if(!UPMBankWSSkeleton.getSesionesActivadas().isEmpty() && r.getResponse()==true){
			for(int i= 0; i<UPMBankWSSkeleton.getSesionesActivadas().size(); i++){
				if(UPMBankWSSkeleton.getSesionesActivadas().get(i).getName().equals(removeUser.getArgs0().getUsername())){
					UPMBankWSSkeleton.getSesionesActivadas().remove(i);
					i--;
				}
			}
		}
		return borrado;
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param addWithdrawal 
	 * @return addWithdrawalResponse 
	 */

	public es.upm.fi.sos.upmbank.AddWithdrawalResponse addWithdrawal
	(es.upm.fi.sos.upmbank.AddWithdrawal addWithdrawal)
	{
		AddWithdrawalResponse retirar = new AddWithdrawalResponse();
		AddMovementResponse movimiento = new AddMovementResponse();
		movimiento.setResult(false);
		retirar.set_return(movimiento);
		//Si el usuario no ha hecho login 
		if(!this.estamosDentro || !this.tengoSesion(UsuarioActivo)){
			return retirar;
		}
		//Si la cuenta existe
		if(this.UsuarioActivo!= null && UPMBankWSSkeleton.getCuentasDelBanco().containsKey(UsuarioActivo.getName())!=false){
			for(int i = 0; i< UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).size(); i++){
				if(UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).get(i).getKey().getIBAN().equals(addWithdrawal.localArgs0.getIBAN())){
					Double saldoInicial = UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).get(i).getValue();
					if((saldoInicial-addWithdrawal.localArgs0.getQuantity())>=0){
						saldoInicial -= addWithdrawal.localArgs0.getQuantity();
						BankAccount cuenta = UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).get(i).getKey();
						UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).set(i, new Pair<>(cuenta,saldoInicial));
						if(UPMBankWSSkeleton.getMovimientosCuenta().containsKey(UsuarioActivo.getName())==false){
							ArrayList<Pair<String,Double>> movimeintos = new ArrayList<Pair<String,Double>>();
							movimeintos.add(new Pair<>(addWithdrawal.localArgs0.getIBAN(),addWithdrawal.localArgs0.getQuantity()));
							UPMBankWSSkeleton.getMovimientosCuenta().put(UsuarioActivo.getName(),movimeintos);
						}
						else{
							UPMBankWSSkeleton.getMovimientosCuenta().get(UsuarioActivo.getName()).add(new Pair<>(addWithdrawal.localArgs0.getIBAN(),addWithdrawal.localArgs0.getQuantity()));
						}
						movimiento.setResult(true);
						movimiento.setBalance(saldoInicial);
						retirar.set_return(movimiento);
						return retirar;
					}
				}
			}		
		}
		return retirar;
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param addUser 
	 * @return addUserResponse 
	 * @throws RemoteException 
	 */

	public es.upm.fi.sos.upmbank.AddUserResponse addUser
	(es.upm.fi.sos.upmbank.AddUser addUser) throws RemoteException{
		AddUserResponse userResponse = new AddUserResponse();
		es.upm.fi.sos.upmbank.xsd.AddUserResponse userResponseA = new es.upm.fi.sos.upmbank.xsd.AddUserResponse();
		userResponseA.setResponse(false);
		userResponse.set_return(userResponseA);
		if(!this.estamosDentro || (this.estamosDentro && this.UsuarioActivo!=null && !this.UsuarioActivo.getName().equals("admin"))
				|| !this.tengoSesion(UsuarioActivo)){
			return userResponse;
		}
		UPMAuthenticationAuthorizationWSSkeletonStub.AddUser usuarioNuevo = new UPMAuthenticationAuthorizationWSSkeletonStub.AddUser();
		UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponseBackEnd usuarioB = new 	UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponseBackEnd();
		UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd usuario = new UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd();
		usuario.setName(addUser.getArgs0().getUsername());
		usuarioNuevo.setUser(usuario);
		//El usuario ya existe en el sistema
		usuarioB = upm.addUser(usuarioNuevo).get_return();
		if(usuarioB.getResult()==false){
			return userResponse;
		}
		userResponseA.setPwd(usuarioB.getPassword());
		userResponseA.setResponse(true);
		userResponse.set_return(userResponseA);
		return userResponse;
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param addIncome 
	 * @return addIncomeResponse 
	 */

	public es.upm.fi.sos.upmbank.AddIncomeResponse addIncome
	(es.upm.fi.sos.upmbank.AddIncome addIncome)
	{
		AddIncomeResponse ingresar = new AddIncomeResponse();
		AddMovementResponse movimiento = new AddMovementResponse();
		movimiento.setResult(false);
		ingresar.set_return(movimiento);
		//Si el usuario no ha hecho login 
		if(!this.estamosDentro || !this.tengoSesion(UsuarioActivo)){
			return ingresar;
		}
		//Si la cuenta no existe
		if(this.UsuarioActivo!= null && UPMBankWSSkeleton.getCuentasDelBanco().containsKey(UsuarioActivo.getName())!=false){
			for(int i = 0; i< UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).size(); i++){
				if(UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).get(i).getKey().getIBAN().equals(addIncome.localArgs0.getIBAN())){
					Double saldoInicial = UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).get(i).getValue();
					saldoInicial += addIncome.localArgs0.getQuantity();
					BankAccount cuenta = UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).get(i).getKey();
					UPMBankWSSkeleton.getCuentasDelBanco().get(UsuarioActivo.getName()).set(i, new Pair<>(cuenta,saldoInicial));
					if(UPMBankWSSkeleton.getMovimientosCuenta().containsKey(UsuarioActivo.getName())==false){
						ArrayList<Pair<String,Double>> movimeintos = new ArrayList<Pair<String,Double>>();
						movimeintos.add(new Pair<>(addIncome.localArgs0.getIBAN(),addIncome.localArgs0.getQuantity()));
						UPMBankWSSkeleton.getMovimientosCuenta().put(UsuarioActivo.getName(),movimeintos);
					}
					else{
						UPMBankWSSkeleton.getMovimientosCuenta().get(UsuarioActivo.getName()).add(new Pair<>(addIncome.localArgs0.getIBAN(),addIncome.localArgs0.getQuantity()));
					}
					movimiento.setResult(true);
					movimiento.setBalance(saldoInicial);
					ingresar.set_return(movimiento);
					return ingresar;
				}
			}		
		}
		return ingresar;
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param login 
	 * @return loginResponse 
	 * @throws RemoteException 
	 */

	public es.upm.fi.sos.upmbank.LoginResponse login
	(es.upm.fi.sos.upmbank.Login login) throws RemoteException {
		//Creamos el loginResponse que va a devolver el metodo
		LoginResponse loginr = new LoginResponse();
		//Creamos el response que dice si la operacion es un exito o no 
		Response respuesta = new Response();
		//Si el usuario ya esta dentro 
		if (this.estamosDentro && this.UsuarioActivo!=null && 
				this.UsuarioActivo.getName().equals(login.localArgs0.getName())){
			respuesta.setResponse(true);
			loginr.set_return(respuesta);
			return loginr;
		}
		//Si el usuario que pide el login no es el que está dentro actualmente 
		if (this.estamosDentro && this.UsuarioActivo!=null && 
				!this.UsuarioActivo.getName().equals(login.localArgs0.getName()) ){
			respuesta.setResponse(false);
			loginr.set_return(respuesta);
			return loginr;
		}
		//Si no hay ningun usuario dentro y el que se quiere loggear es el admin 
		if(!this.estamosDentro && login.getArgs0().getName().equals("admin")){
			this.estamosDentro = true;
			this.UsuarioActivo.setName("admin");
			this.UsuarioActivo.setPwd("admin");
			UPMBankWSSkeleton.getSesionesActivadas().add(UsuarioActivo);
			respuesta.setResponse(true);
			loginr.set_return(respuesta);
			return loginr;
		}
		//Si no hay ningun usuario dentro y el que se quiere loggear NO es el admin 
		if (!this.estamosDentro && !login.getArgs0().getName().equals("admin")){
			UPMAuthenticationAuthorizationWSSkeletonStub.Login servio = new UPMAuthenticationAuthorizationWSSkeletonStub.Login();
			UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd loginend = new UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd();
			UPMAuthenticationAuthorizationWSSkeletonStub.LoginResponseBackEnd servioL = new UPMAuthenticationAuthorizationWSSkeletonStub.LoginResponseBackEnd();
			loginend.setName(login.localArgs0.getName());
			loginend.setPassword(login.localArgs0.getPwd());
			servio.setLogin(loginend);
			servioL = upm.login(servio).get_return();
			if(servioL.getResult()==true){
				estamosDentro = true;
				this.UsuarioActivo.setName(login.getArgs0().getName());
				this.UsuarioActivo.setPwd(login.getArgs0().getPwd());
				UPMBankWSSkeleton.getSesionesActivadas().add(UsuarioActivo);
				respuesta.setResponse(true);
				loginr.set_return(respuesta);
				return loginr;
			}
			else {
				respuesta.setResponse(false);
				loginr.set_return(respuesta);
				return loginr;
			}
		}
		return loginr;
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param getMyMovements 
	 * @return getMyMovementsResponse 
	 */

	public es.upm.fi.sos.upmbank.GetMyMovementsResponse getMyMovements
	(es.upm.fi.sos.upmbank.GetMyMovements getMyMovements)
	{
		GetMyMovementsResponse movimientos = new GetMyMovementsResponse();
		MovementList movimientoList = new MovementList();
		movimientoList.setResult(false);
		movimientos.set_return(movimientoList);
		//Si el usuario no ha hecho login 
		if(!this.estamosDentro || !this.tengoSesion(UsuarioActivo)){
			return movimientos;
		}
		double [] mov = new double[10];
		if(!UPMBankWSSkeleton.getMovimientosCuenta().isEmpty()){
			for(int i = 0, j=UPMBankWSSkeleton.getMovimientosCuenta().get(UsuarioActivo.getName()).size()-1; i< mov.length && j>=0; i++, j--){
				mov[i] = UPMBankWSSkeleton.getMovimientosCuenta().get(UsuarioActivo.getName()).get(j).getValue();
			}
		}
		movimientoList.setMovementQuantities(mov);
		movimientoList.setResult(true);
		movimientos.set_return(movimientoList);
		return movimientos;
	}


	/**
	 * Auto generated method signature
	 * 
	 * @param changePassword 
	 * @return changePasswordResponse 
	 * @throws RemoteException 
	 */

	public es.upm.fi.sos.upmbank.ChangePasswordResponse changePassword
	(es.upm.fi.sos.upmbank.ChangePassword changePassword) throws RemoteException{
		ChangePasswordResponse cambiote = new ChangePasswordResponse();
		Response respuesta = new Response();
		respuesta.setResponse(false);
		cambiote.set_return(respuesta);
		//Si no esta el usuario dentro
		if(!this.estamosDentro || !this.tengoSesion(UsuarioActivo)){
			return cambiote;
		}
		//Si las contraseñas antiguas no coinciden
		if(this.UsuarioActivo!= null && !this.UsuarioActivo.getPwd().equals(changePassword.localArgs0.getOldpwd())){
			return cambiote;
		}
		UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword cambiar = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword();
		UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd cambiarE = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd();
		UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordResponse cambiarR = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordResponse();
		cambiarE.setName(this.UsuarioActivo.getName());
		cambiarE.setNewpwd(changePassword.localArgs0.getNewpwd());
		cambiarE.setOldpwd(changePassword.localArgs0.getOldpwd());
		cambiar.setChangePassword(cambiarE);
		cambiarR = upm.changePassword(cambiar).get_return();
		if(cambiarR.getResult()==true){
			UPMBankWSSkeleton.getSesionesActivadas().remove(UsuarioActivo);
			this.UsuarioActivo.setPwd(changePassword.localArgs0.getNewpwd());			
			UPMBankWSSkeleton.getSesionesActivadas().add(this.UsuarioActivo);
			respuesta.setResponse(true);
			cambiote.set_return(respuesta);
		}
		return cambiote;
	}

	public static ArrayList<User> getSesionesActivadas() {
		return sesionesActivadas;
	}

	public static void setSesionesActivadas(ArrayList<User> sesionesActivadas) {
		UPMBankWSSkeleton.sesionesActivadas = sesionesActivadas;
	}

	public static HashMap<String, ArrayList<Pair<String, Double>>> getMovimientosCuenta() {
		return movimientosCuenta;
	}

	public static void setMovimientosCuenta(HashMap<String,ArrayList<Pair<String,Double>>> movimientosCuenta) {
		UPMBankWSSkeleton.movimientosCuenta = movimientosCuenta;
	}

	public static HashMap<String,ArrayList<Pair<BankAccount,Double>>> getCuentasDelBanco() {
		return cuentasDelBanco;
	}

	public static void setCuentasDelBanco(HashMap<String,ArrayList<Pair<BankAccount,Double>>> cuentasDelBanco) {
		UPMBankWSSkeleton.cuentasDelBanco = cuentasDelBanco;
	}

	private boolean tengoSesion (User usuario){
		if(!UPMBankWSSkeleton.getSesionesActivadas().isEmpty()){
			for (int i = 0; i< UPMBankWSSkeleton.getSesionesActivadas().size(); i++){
				if(UPMBankWSSkeleton.getSesionesActivadas().get(i).getName().equals(usuario.getName())){
					return true;
				}
			}
		}
		return false;
	}

}

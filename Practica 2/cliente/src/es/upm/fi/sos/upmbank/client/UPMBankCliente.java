package es.upm.fi.sos.upmbank.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import es.upm.fi.sos.upmbank.client.*;
import es.upm.fi.sos.upmbank.client.UPMBankWSStub.BankAccountResponse;

public class UPMBankCliente {

	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub
		UPMBankWSStub upm = new UPMBankWSStub();
		upm._getServiceClient().engageModule("addressing");
		upm._getServiceClient().getOptions().setManageSession(true);

		//******************************Datos iniciales para las pruebas*******************//

		UPMBankWSStub.User admin = new UPMBankWSStub.User();
		admin.setName("admin");
		admin.setPwd("admin");

		UPMBankWSStub.User usuario1 = new UPMBankWSStub.User();
		usuario1.setName("usuario1565718");

		UPMBankWSStub.Username usuario1N = new UPMBankWSStub.Username();
		usuario1N.setUsername(usuario1.getName());

		UPMBankWSStub.User usuario2 = new UPMBankWSStub.User();
		usuario2.setName("soyusuario37590");

		UPMBankWSStub.User usuario3 = new UPMBankWSStub.User();	
		usuario3.setName("otrousuario");

		//Hacer un login valido
		System.out.println("Hacer un login de admin para comprobar el añadir usuarios");
		System.out.println("Se espera TRUE");
		UPMBankWSStub.Login login = new UPMBankWSStub.Login();
		login.setArgs0(admin);
		UPMBankWSStub.LoginResponse respuestaL = new UPMBankWSStub.LoginResponse();
		respuestaL = upm.login(login);
		System.out.println("Resultado " +respuestaL.get_return().getResponse());

		//Añadir un usuario valido
		System.out.println("Añadir un usuario valido");
		System.out.println("Se espera TRUE");
		UPMBankWSStub.AddUser añadir = new UPMBankWSStub.AddUser();
		UPMBankWSStub.Username username = new UPMBankWSStub.Username();
		username.setUsername(usuario1.getName());
		añadir.setArgs0(username);
		UPMBankWSStub.AddUserResponse añadirR = new UPMBankWSStub.AddUserResponse();
		añadirR = upm.addUser(añadir).get_return();
		System.out.println("Resultado " + añadirR.getResponse());
		System.out.println("Contraseña " + añadirR.getPwd());
		usuario1.setPwd(añadirR.getPwd());

		//Añadir un usuario que ya existe
		System.out.println("Añadir un usuario que ya existe");
		System.out.println("Se espera FALSE");
		username.setUsername(usuario1.getName());
		añadir.setArgs0(username);
		añadirR = upm.addUser(añadir).get_return();
		System.out.println("Resultado " + añadirR.getResponse());

		//Hacer un logout para preparar las pruebas de login 
		System.out.println("Hacer un logout para preparar las pruebas de login ");
		System.out.println("Se espera TRUE");
		UPMBankWSStub.Logout salir = new UPMBankWSStub.Logout();
		upm.logout(salir);

		//Hacer un login valido
		System.out.println("Hacer un login valido");
		System.out.println("Se espera TRUE");
		login.setArgs0(usuario1);
		respuestaL = upm.login(login);
		System.out.println("Resultado " +respuestaL.get_return().getResponse());

		//Llamadas repetidas a login
		System.out.println("Llamadas repetidas a login");
		System.out.println("Se espera TRUE");
		login.setArgs0(usuario1);
		respuestaL = upm.login(login);
		System.out.println("Resultado " +respuestaL.get_return().getResponse());


		//Conectarse con un usuario que no exista
		System.out.println("Conectarse con un usuario que no exista");
		System.out.println("Se espera FALSE");
		login.setArgs0(usuario2);
		respuestaL = upm.login(login);
		System.out.println("Resultado " +respuestaL.get_return().getResponse());

		//*****************PRUEBAS DE CHANGEPASSWORD********************/
		System.out.println("//*****************PRUEBAS DE CHANGEPASSWORD********************//");

		//Cambio de contraseña valido
		System.out.println("Cambio de contraseña valido");
		System.out.println("Se espera TRUE");
		UPMBankWSStub.ChangePassword cambiar = new 	UPMBankWSStub.ChangePassword();
		UPMBankWSStub.PasswordPair contrasenas = new UPMBankWSStub.PasswordPair();
		contrasenas.setOldpwd(usuario1.getPwd());
		contrasenas.setNewpwd("nuevacontrasena1");
		cambiar.setArgs0(contrasenas);
		UPMBankWSStub.ChangePasswordResponse cambiarR = new UPMBankWSStub.ChangePasswordResponse();
		cambiarR = upm.changePassword(cambiar);
		System.out.println("Resultado " + cambiarR.get_return().getResponse());
		usuario1.setPwd(contrasenas.getNewpwd());


		//Error si la contraseña antigua no es correcta
		System.out.println("Cambio de contraseña con contraseña antigua incorrecta");
		System.out.println("Se espera FALSE");
		contrasenas.setOldpwd("estotienemalapinta");
		contrasenas.setNewpwd("nuevacontrasena1");
		cambiar.setArgs0(contrasenas);
		cambiarR = upm.changePassword(cambiar);
		System.out.println("Resultado " + cambiarR.get_return().getResponse());


		//Crear una cuenta correctamente 
		System.out.println("Crear una cuenta correctamente ");
		System.out.println("Se espera TRUE");
		UPMBankWSStub.AddBankAcc cuentanueva = new UPMBankWSStub.AddBankAcc();
		UPMBankWSStub.Deposit cantidad = new UPMBankWSStub.Deposit();
		Double dinero = new Double(100);
		cantidad.setQuantity(dinero);
		cuentanueva.setArgs0(cantidad);
		UPMBankWSStub.BankAccountResponse cuentanuevaR = new UPMBankWSStub.BankAccountResponse();
		cuentanuevaR = upm.addBankAcc(cuentanueva).get_return();
		System.out.println("Resultado " + cuentanuevaR.getResult());
		System.out.println("IBAN " + cuentanuevaR.getIBAN());
		UPMBankWSStub.BankAccount cuentaCreada1 = new UPMBankWSStub.BankAccount();
		cuentaCreada1.setIBAN(cuentanuevaR.getIBAN());

		//Hacer un ingreso correctamente 
		System.out.println("Hacer un ingreso correctamente");
		System.out.println("Se espera TRUE");
		UPMBankWSStub.AddIncome ingreso = new UPMBankWSStub.AddIncome();
		UPMBankWSStub.Movement movimientoI = new UPMBankWSStub.Movement();
		movimientoI.setIBAN(cuentaCreada1.getIBAN());
		movimientoI.setQuantity(50.00);
		ingreso.setArgs0(movimientoI);
		UPMBankWSStub.AddMovementResponse ingresoR = new UPMBankWSStub.AddMovementResponse();
		ingresoR = upm.addIncome(ingreso).get_return();
		System.out.println("Resultado " + ingresoR.getResult());
		System.out.println("Balance " + ingresoR.getBalance());

		//Intentamos borrar una cuenta que tiene saldo
		System.out.println("Intentamos borrar una cuenta que tiene saldo");
		System.out.println("Se espera FALSE");
		UPMBankWSStub.CloseBankAcc cerrar = new UPMBankWSStub.CloseBankAcc();
		UPMBankWSStub.CloseBankAccResponse cerrarR = new UPMBankWSStub.CloseBankAccResponse();
		cerrar.setArgs0(cuentaCreada1);
		cerrarR = upm.closeBankAcc(cerrar);
		System.out.println("Resultdo " + cerrarR.get_return().getResponse());

		//Hacer una retirada correctamente 
		System.out.println("Hacer una retirada correctamente ");
		System.out.println("Se espera TRUE");
		UPMBankWSStub.AddWithdrawal retirada = new UPMBankWSStub.AddWithdrawal();
		movimientoI.setIBAN(cuentaCreada1.getIBAN());
		movimientoI.setQuantity(25.00);
		retirada.setArgs0(movimientoI);
		UPMBankWSStub.AddWithdrawalResponse retiradaR = new UPMBankWSStub.AddWithdrawalResponse();
		retiradaR = upm.addWithdrawal(retirada);
		System.out.println("Resultado " + retiradaR.get_return().getResult());
		System.out.println("Balance " + retiradaR.get_return().getBalance());

		//Intentamos hacer una retirada de dinero con saldo insuficiente 
		System.out.println("Intentamos hacer una retirada de dinero con saldo insuficiente ");
		System.out.println("Se espera FALSE");
		movimientoI.setIBAN(cuentaCreada1.getIBAN());
		movimientoI.setQuantity(250.00);
		retirada.setArgs0(movimientoI);
		retiradaR = upm.addWithdrawal(retirada);
		System.out.println("Resultado " + retiradaR.get_return().getResult());
		System.out.println("Balance " + retiradaR.get_return().getBalance());

		//Borrar una cuenta cuando tiene saldo
		System.out.println("Borrar una cuenta cuando tiene saldo ");
		System.out.println("Se espera FALSE");
		UPMBankWSStub.CloseBankAcc cerrarCuenta = new UPMBankWSStub.CloseBankAcc();
		cerrarCuenta.setArgs0(cuentaCreada1);
		UPMBankWSStub.CloseBankAccResponse cerrarCuentaR = new UPMBankWSStub.CloseBankAccResponse();
		cerrarCuentaR = upm.closeBankAcc(cerrarCuenta);
		System.out.println("Resultado " + cerrarCuentaR.get_return().getResponse());

		//Hacer una retirada correctamente
		System.out.println("Hacer una retirada correctamente");
		System.out.println("Se espera TRUE");
		movimientoI.setIBAN(cuentaCreada1.getIBAN());
		movimientoI.setQuantity(125.00);
		retirada.setArgs0(movimientoI);
		retiradaR = upm.addWithdrawal(retirada);
		System.out.println("Resultado " + retiradaR.get_return().getResult());
		System.out.println("Balance " + retiradaR.get_return().getBalance());

		//Imprimir los ultimos 10 movimientos
		System.out.println("Imprimir los ultimos 10 movimientos");
		System.out.println("Se espera MOVIMIENTOS");
		UPMBankWSStub.GetMyMovements movimientos = new UPMBankWSStub.GetMyMovements();
		UPMBankWSStub.GetMyMovementsResponse movimientosR = new UPMBankWSStub.GetMyMovementsResponse();
		movimientosR = upm.getMyMovements(movimientos);
		System.out.println("Resultado " + movimientosR.get_return().getResult());
		for (int i = 0; i< movimientosR.get_return().getMovementQuantities().length; i++){
			System.out.print(movimientosR.get_return().getMovementQuantities()[i] + ", ");
		}
		System.out.print("\n");

		//Borrar usuario con cuentas abiertas 
		System.out.println("Borrar usuario con cuentas abiertas siendo distinto del admin");
		System.out.println("Se espera FALSE");
		UPMBankWSStub.RemoveUser borrar = new UPMBankWSStub.RemoveUser();
		borrar.setArgs0(usuario1N);
		UPMBankWSStub.Response borrarR = new UPMBankWSStub.Response();
		borrarR = upm.removeUser(borrar).get_return();
		System.out.println("Resultado " + borrarR.getResponse());


		//Borrar una cuenta correctamente 
		System.out.println("Borrar una cuenta correctamente ");
		System.out.println("Se espera TRUE");
		cerrarCuenta.setArgs0(cuentaCreada1);
		cerrarCuentaR = upm.closeBankAcc(cerrarCuenta);
		System.out.println("Resultado " + cerrarCuentaR.get_return().getResponse());

		//Intentar hacer un ingreso a cuenta borrada
		System.out.println("Intentar hacer un ingreso a cuenta borrada");
		System.out.println("Se espera FALSE");
		movimientoI.setIBAN(cuentaCreada1.getIBAN());
		movimientoI.setQuantity(50.00);
		ingreso.setArgs0(movimientoI);
		ingresoR = upm.addIncome(ingreso).get_return();
		System.out.println("Resultado " + ingresoR.getResult());
		System.out.println("Balance " + ingresoR.getBalance());

		//Imprimir los ultimos 10 movimientos
		System.out.println("Imprimir los ultimos 10 movimientos");
		System.out.println("Se espera VACIO");
		movimientosR = upm.getMyMovements(movimientos);
		System.out.println("Resultado " + movimientosR.get_return().getResult());
		for (int i = 0; i< movimientosR.get_return().getMovementQuantities().length; i++){
			System.out.print(movimientosR.get_return().getMovementQuantities()[i] + ", ");
		}
		System.out.print("\n");

		//Borrar un usuario por otro usuario que no es el admin
		System.out.println("Borrar un usuario por otro usuario que no es el admin");
		System.out.println("Se espera FALSE");
		borrar.setArgs0(usuario1N);
		borrarR = upm.removeUser(borrar).get_return();
		System.out.println("Resultado " + borrarR.getResponse());


		//Hacer un logout para preparar las pruebas de login 
		System.out.println("Hacer un logout para preparar las pruebas con varios clientes ");
		System.out.println("Se espera TRUE");
		upm.logout(salir);

		//********************PRUEBAS CON VARIOS CLIENTES*************************
		UPMBankWSStub upm1 = new UPMBankWSStub();
		upm1._getServiceClient().engageModule("addressing");
		upm1._getServiceClient().getOptions().setManageSession(true);

		UPMBankWSStub upm2 = new UPMBankWSStub();
		upm2._getServiceClient().engageModule("addressing");
		upm2._getServiceClient().getOptions().setManageSession(true);

		//Hacer un login valido
		System.out.println("Hacer un login de admin para comprobar el añadir usuarios");
		System.out.println("Se espera TRUE");
		login.setArgs0(admin);
		respuestaL = upm1.login(login);
		System.out.println("Resultado " +respuestaL.get_return().getResponse());

		//Añadir un usuario valido
		System.out.println("Añadir un usuario valido");
		System.out.println("Se espera TRUE");
		username.setUsername(usuario2.getName());
		añadir.setArgs0(username);
		añadirR = upm1.addUser(añadir).get_return();
		System.out.println("Resultado " + añadirR.getResponse());
		System.out.println("Contraseña " + añadirR.getPwd());
		usuario2.setPwd(añadirR.getPwd());

		//Hacer un login del usuario creado por otro cliente en nueva sesion
		System.out.println("Hacer un login del usuario creado por otro cliente en nueva sesion");
		System.out.println("Se espera TRUE");
		login.setArgs0(usuario2);
		respuestaL = upm2.login(login);
		System.out.println("Resultado " +respuestaL.get_return().getResponse());

		//Crear una cuenta correctamente 
		System.out.println("Crear una cuenta correctamente ");
		System.out.println("Se espera TRUE");
		Double dinero1 = new Double(1000);
		cantidad.setQuantity(dinero1);
		cuentanueva.setArgs0(cantidad);
		cuentanuevaR = upm2.addBankAcc(cuentanueva).get_return();
		System.out.println("Resultado " + cuentanuevaR.getResult());
		System.out.println("IBAN " + cuentanuevaR.getIBAN());
		UPMBankWSStub.BankAccount cuentaCreada2 = new UPMBankWSStub.BankAccount();
		cuentaCreada2.setIBAN(cuentanuevaR.getIBAN());

		//Hacer un ingreso correctamente 
		for (int i = 0; i < 5; i++){
			System.out.println("Hacer un ingreso correctamente");
			System.out.println("Se espera TRUE");
			movimientoI.setIBAN(cuentaCreada2.getIBAN());
			Double cantidadIngreso = 25.00;
			movimientoI.setQuantity(cantidadIngreso);
			ingreso.setArgs0(movimientoI);
			ingresoR = upm2.addIncome(ingreso).get_return();
			System.out.println("Resultado " + ingresoR.getResult());
			System.out.println("Balance " + ingresoR.getBalance());
			cantidadIngreso = cantidadIngreso+2;
		}

		//Cerramos la sesion del cliente 
		upm2.logout(salir);
		//Volvemos a conectarnos 
		login.setArgs0(usuario2);
		respuestaL = upm2.login(login);
		//Imprimir los ultimos 10 movimientos
		System.out.println("Imprimir los ultimos 10 movimientos tras haber cerrado la sesion y volverla a abrir");
		System.out.println("Se espera MOVIMIENTOS");
		movimientosR = upm2.getMyMovements(movimientos);
		System.out.println("Resultado " + movimientosR.get_return().getResult());
		for (int i = 0; i< movimientosR.get_return().getMovementQuantities().length; i++){
			System.out.print(movimientosR.get_return().getMovementQuantities()[i] + ", ");
		}
		System.out.print("\n");


		for (int i = 0; i <6; i++){
			//Hacer una retirada correctamente
			System.out.println("Hacer una retirada correctamente");
			System.out.println("Se espera TRUE");
			movimientoI.setIBAN(cuentaCreada2.getIBAN());
			Double cantidadRet = 5.00;
			movimientoI.setQuantity(cantidadRet);
			retirada.setArgs0(movimientoI);
			retiradaR = upm2.addWithdrawal(retirada);
			System.out.println("Resultado " + retiradaR.get_return().getResult());
			System.out.println("Balance " + retiradaR.get_return().getBalance());
			cantidadRet = cantidadRet+2;
		}

		//Imprimir los ultimos 10 movimientos
		System.out.println("Imprimir los ultimos 10 movimientos");
		System.out.println("Se espera MOVIMIENTOS");
		movimientosR = upm2.getMyMovements(movimientos);
		System.out.println("Resultado " + movimientosR.get_return().getResult());
		for (int i = 0; i< movimientosR.get_return().getMovementQuantities().length; i++){
			System.out.print(movimientosR.get_return().getMovementQuantities()[i] + ", ");
		}
		System.out.print("\n");

		//Borrar una cuenta cuando tiene saldo
		System.out.println("Borrar una cuenta cuando tiene saldo ");
		System.out.println("Se espera FALSE");
		cerrarCuenta.setArgs0(cuentaCreada2);
		cerrarCuentaR = upm2.closeBankAcc(cerrarCuenta);
		System.out.println("Resultado " + cerrarCuentaR.get_return().getResponse());

		//Borrar una cuenta cuando no eres el dueñi de dicha cuenta 
		System.out.println("Borrar una cuenta cuando no eres el dueño de dicha cuenta ");
		System.out.println("Se espera FALSE");
		cerrarCuenta.setArgs0(cuentaCreada2);
		cerrarCuentaR = upm1.closeBankAcc(cerrarCuenta);
		System.out.println("Resultado " + cerrarCuentaR.get_return().getResponse());

		//Borrar un usuario por admin cuando tiene cuentas abiertas
		System.out.println("Borrar un usuario por admin cuando tiene cuentas abiertas");
		System.out.println("Se espera FALSE");
		username.setUsername(usuario2.getName());
		borrar.setArgs0(username);
		borrarR = upm1.removeUser(borrar).get_return();
		System.out.println("Resultado " + borrarR.getResponse());


		//Hacer una retirada correctamente
		System.out.println("Hacer una retirada correctamente");
		System.out.println("Se espera TRUE");
		movimientoI.setIBAN(cuentaCreada2.getIBAN());
		Double cantidadRet = 1095.00;
		movimientoI.setQuantity(cantidadRet);
		retirada.setArgs0(movimientoI);
		retiradaR = upm2.addWithdrawal(retirada);
		System.out.println("Resultado " + retiradaR.get_return().getResult());
		System.out.println("Balance " + retiradaR.get_return().getBalance());

		//Borrar una cuenta correctamente 
		System.out.println("Borrar una cuenta correctamente ");
		System.out.println("Se espera TRUE");
		cerrarCuenta.setArgs0(cuentaCreada2);
		cerrarCuentaR = upm2.closeBankAcc(cerrarCuenta);
		System.out.println("Resultado " + cerrarCuentaR.get_return().getResponse());

		//Borrar un usuario por otro usuario que no es el admin
		System.out.println("Borrar un usuario por otro usuario distinto al admin que quiere borrar al superuser");
		System.out.println("Se espera FALSE");
		username.setUsername(admin.getName());
		borrar.setArgs0(username);
		borrarR = upm2.removeUser(borrar).get_return();
		System.out.println("Resultado " + borrarR.getResponse());


		//Borrar un usuario por otro usuario que no es el admin
		System.out.println("Borrar un usuario correctamente");
		System.out.println("Se espera TRUE");
		username.setUsername(usuario1.getName());
		borrar.setArgs0(username);
		borrarR = upm1.removeUser(borrar).get_return();
		System.out.println("Resultado " + borrarR.getResponse());

		//Borrar un usuario por otro usuario que no es el admin
		System.out.println("Borrar un usuario correctamente");
		System.out.println("Se espera TRUE");
		username.setUsername(usuario2.getName());
		borrar.setArgs0(username);
		borrarR = upm1.removeUser(borrar).get_return();
		System.out.println("Resultado " + borrarR.getResponse());

		//Intentar crear una cuenta con usuario borrado en otra sesion por admin
		System.out.println("Intentar crear una cuenta con usuario borrado en otra sesion por admin ");
		System.out.println("Se espera FALSE");
		Double dinero2 = new Double(50.00);
		cantidad.setQuantity(dinero2);
		cuentanueva.setArgs0(cantidad);
		cuentanuevaR = upm2.addBankAcc(cuentanueva).get_return();
		System.out.println("Resultado " + cuentanuevaR.getResult());


	}

}

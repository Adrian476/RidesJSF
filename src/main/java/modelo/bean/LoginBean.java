package modelo.bean;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.ApplicationScoped;
import modelo.rideExceptions.*;

import jakarta.inject.Named;
import modelo.rideBusinessLogic.BLFacade;
import modelo.rideBusinessLogic.BLFacadeImplementation;
import modelo.rideDominio.Driver;

@Named("login")
@ApplicationScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String mail;
	private String password;

	private Driver user;
	private final BLFacade facade;

	public LoginBean() {
		facade = new BLFacadeImplementation();
	}

	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Driver getUser() {
		return user;
	}

	public void setUser(Driver user) {
		this.user = user;
	}

	public String logIn() {
		try {
			Driver user = facade.logIn(mail, password);
			if (user == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario no encontrado"));;
				return null;
			}
			this.setUser(user);
			return "ok";
		}catch (IncorrectPasswordException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Contraseña incorrecta"));
			return null;
		}catch (IllegalArgumentException e2) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Completa todos los campos"));
			return "ilegal argument";
		}


	}
} 

package modelo.bean;

import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import modelo.rideBusinessLogic.BLFacade;
import modelo.rideBusinessLogic.BLFacadeImplementation;
import modelo.rideDominio.Driver;

@SuppressWarnings("serial")
@Named("registerBean")
@RequestScoped
public class RegisterBean implements Serializable {

    private String name;
    private String mail;
    private String password;
    private String confirmPassword;

    private final BLFacade facade;

    public RegisterBean() {
        this.facade = new BLFacadeImplementation();
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public void register() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (!password.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Las contraseñas no coinciden", null));
            return;
        }
        try {
            Driver newDriver = facade.registerDriver(mail, name, password);
            if (newDriver != null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "¡Registro completado! Ya puedes iniciar sesión.", null));
                this.setName(null); this.setMail(null); 
                this.setPassword(null);this.setConfirmPassword(null);
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "El email ya está registrado.", null));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al registrar: " + e.getMessage(), null));
        }
    }
}
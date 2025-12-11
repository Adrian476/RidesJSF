package modelo.bean;
import java.io.Serializable;
import java.util.*;
import modelo.rideBusinessLogic.*;
import modelo.rideDominio.Driver;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("createRides")
@SessionScoped
public class CreateRidesBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fromCity;
    private String toCity;
    private Date date; 
    private String seats;
    private String price;
    private String driverEmail = "DriverTestJSF";

    private BLFacade facade;
    
    public CreateRidesBean() {
    	this.facade = new BLFacadeImplementation();
    }
    
    public String createRides() {
    	try {
    		
    		facade.createRide(fromCity, toCity, date, Integer.parseInt(seats.trim()), Float.parseFloat(price.trim()), driverEmail);
    		System.out.println("Viaje correctamente creado");
    		return "viaje creado";
    	} catch (Exception e){
    		e.printStackTrace();
    		return "error create";
    	}
    }
    
    public String getFromCity() { 
    	return fromCity; 
    }

    public void setFromCity(String fromCity) { 
    	this.fromCity = fromCity; 
    }

    public String getToCity() {
    	return toCity; 
    }

    public void setToCity(String toCity) {
    	this.toCity = toCity; 
    }

    public Date getDate() { 
    	return date;
    }

    public void setDate(Date date) { 
    	this.date = date; 
    }

    public String getSeats() { 
    	return seats; 
    }

    public void setSeats(String seats) { 
    	this.seats = seats; 
    }

    public String getPrice() { 
    	return price; 
    }

    public void setPrice(String price) { 
    	this.price = price; 
    }
    
    public String getDriverEmail() {
    	return driverEmail;
    }
    
    public void setDriverEmail(String driverEmail) {
    	this.driverEmail = driverEmail;
    }

}

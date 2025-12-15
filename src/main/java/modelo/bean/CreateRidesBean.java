package modelo.bean;
import java.io.Serializable;
import java.util.*;
import modelo.rideBusinessLogic.*;

import modelo.rideDominio.Ride;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SuppressWarnings("serial")
@Named("createRides")
@SessionScoped
public class CreateRidesBean implements Serializable{

	private String fromCity;
	private String toCity;
	private Date date; 
	private String seats;
	private String price;
	private String message;


	@Inject
    private LoginBean loginBean;
	private BLFacade facade;

	public CreateRidesBean() {
		this.facade = new BLFacadeImplementation();
	}

	public String createRides() {
		try {

			Ride r = facade.createRide(fromCity, toCity, date, Integer.parseInt(seats.trim()), Float.parseFloat(price.trim()), loginBean.getMail());
			if (r != null) {
				message = "Ride creado correctamente.";
			} else {
				message = "No se ha podido crear el ride.";
			}

			return "viaje creado";
		} catch (Exception e){
			e.printStackTrace();
			message = "error create";
			return "error create";
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

}
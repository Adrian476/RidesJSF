package modelo.bean;

import java.io.Serializable;
import java.util.*;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Ride;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("queryRides")
@SessionScoped
public class QueryRidesBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private BLFacade facade;

    private List<String> departCities;
    private List<String> arriveCities;

    private String selectedDepart;
    private String selectedArrive;

    private Date selectedDate;

    private List<Ride> rides;

    public QueryRidesBean() {
        facade = new BLFacadeImplementation();
        departCities = facade.getDepartCities();   
        arriveCities = new ArrayList<>();
    }


    public void updateArriveCities() {
        if (selectedDepart != null) {
            arriveCities = facade.getDestinationCities(selectedDepart);
        }
    }


    public void searchRides() {
        if (selectedDepart != null && selectedArrive != null && selectedDate != null) {
            rides = facade.getRides(selectedDepart, selectedArrive, selectedDate);
        } else {
            rides = Collections.emptyList();
        }
    }

    public String getTableTitle() {
        if (rides == null || rides.isEmpty()) {
            return "No hay viajes " + format(selectedDate);
        } else {
            return "Viajes " + format(selectedDate);
        }
    }

    private String format(Date d) {
        if (d == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(d);
    }


    public List<String> getDepartCities() { 
    	return departCities; 
    }
    public List<String> getArriveCities() { 
    	return arriveCities; 
    }

    public String getSelectedDepart() { 
    	return selectedDepart; 
    }
    public void setSelectedDepart(String s) { 
    	this.selectedDepart = s; 
    }

    public String getSelectedArrive() { 
    	return selectedArrive; 
    }
    public void setSelectedArrive(String s) { 
    	this.selectedArrive = s; 
    }

    public Date getSelectedDate() { 
    	return selectedDate; 
    }
    public void setSelectedDate(Date d) { 
    	this.selectedDate = d; 
    }

    public List<Ride> getRides() { 
    	return rides; 
    }
}

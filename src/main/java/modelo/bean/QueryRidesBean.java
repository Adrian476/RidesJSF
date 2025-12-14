package modelo.bean;

import java.io.Serializable;
import java.util.*;
import modelo.rideBusinessLogic.*;
import modelo.rideDominio.*;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;


@SuppressWarnings("serial")
@Named("queryRides")
@ViewScoped
public class QueryRidesBean implements Serializable{
	
	private String selectedDepart;
    String selectedArrive;
    Date selectedDate;
    
	List<Ride> rides = Collections.emptyList();

    List<String> departCities = Collections.emptyList();
    List<String> arriveCities = Collections.emptyList();
    private final BLFacade facade;

    public QueryRidesBean() {
        facade = new BLFacadeImplementation();
        
    }
    
    public void updateArriveCities() {
    	System.out.println("updateArriveCities() -> origen: " + selectedDepart);
        if (selectedDepart != null && !selectedDepart.isEmpty()) {
            arriveCities = facade.getDestinationCities(selectedDepart);
        } else {
            arriveCities = Collections.emptyList();
        }
        selectedArrive = null;
        selectedDate = null;
        rides = Collections.emptyList();
        
    }


    public void searchRides() {
    	System.out.println("searchRides() -> " + selectedDepart + " → " + selectedArrive + " el " + selectedDate);
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
    	if (departCities == null || departCities.isEmpty()) {
            departCities = facade.getDepartCities();
        }
        return departCities;
    }
    public List<String> getArriveCities() { 
    	if (arriveCities == null || arriveCities.isEmpty()) { 
            if (selectedDepart != null && !selectedDepart.isEmpty()) {
                arriveCities = facade.getDestinationCities(selectedDepart);
            }
        }
        return arriveCities;
    }

    public String getSelectedDepart() { 
    	return selectedDepart; 
    }
    public void setSelectedDepart(String s) { 
    	System.out.println("From "+ s);
    	this.selectedDepart = s; 
    }

    public String getSelectedArrive() { 
    	return selectedArrive; 
    }
    public void setSelectedArrive(String s) { 
    	System.out.println("Changing arrival from = " + this.selectedArrive + "to = " + s);
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

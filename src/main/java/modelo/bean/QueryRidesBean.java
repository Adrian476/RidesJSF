package modelo.bean;

import java.io.Serializable;
import java.util.*;
import modelo.rideBusinessLogic.*;
import modelo.rideDominio.*;
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
        arriveCities = facade.getDestinationCities(selectedDepart);
    }


    public void updateArriveCities() {
        //if (selectedDepart != null) 
    	System.out.println("Updating the destinations cities!");
            arriveCities = facade.getDestinationCities(selectedDepart);
        
    }


    public void searchRides() {
    	departCities = facade.getDepartCities();
    	updateArriveCities();
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
    	departCities = facade.getDepartCities();  
    	return departCities; 
    }
    public List<String> getArriveCities() { 
    	arriveCities = facade.getDestinationCities(selectedDepart);
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

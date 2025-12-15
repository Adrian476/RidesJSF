package modelo.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import modelo.rideBusinessLogic.BLFacade;
import modelo.rideBusinessLogic.BLFacadeImplementation;
import modelo.rideDominio.Driver;
import modelo.rideDominio.Ride;

@SuppressWarnings("serial")
@Named("myRides")
@ViewScoped
public class MyRidesBean implements Serializable {

    @Inject
    private LoginBean loginBean; 

    private List<Ride> myRides = Collections.emptyList();
    BLFacade facade;

    public MyRidesBean() {
    	facade = new BLFacadeImplementation();
    }

    public List<Ride> getMyRides() {
    	Driver loggedUser = loginBean.getUser();
        if (loggedUser != null) {
            Driver d = facade.getDriverByEmail(loggedUser.getEmail());
            if (d != null) {
                myRides = d.getRides();
            } else {
                myRides = Collections.emptyList();
            }
        } else {
            myRides = Collections.emptyList();
        }
        return myRides;
    }

  

    public void refresh() {
    	myRides = Collections.emptyList();
    }
}
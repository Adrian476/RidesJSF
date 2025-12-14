package modelo.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import modelo.rideDominio.Driver;
import modelo.rideDominio.Ride;

@SuppressWarnings("serial")
@Named("myRides")
@ViewScoped
public class MyRidesBean implements Serializable {

    @Inject
    private LoginBean loginBean; 

    private List<Ride> myRides = Collections.emptyList();

    public MyRidesBean() {}

    public void loadMyRides() {
        Driver driver = loginBean.getUser();
        if (driver != null) {
            myRides = driver.getRides();
        } else {
            myRides = Collections.emptyList();
        }
    }

    public List<Ride> getMyRides() {
        if (myRides.isEmpty()) {
            loadMyRides();
        }
        return myRides;
    }

    public void refresh() {
        loadMyRides();
    }
}
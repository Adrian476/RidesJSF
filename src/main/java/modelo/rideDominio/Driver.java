package modelo.rideDominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.*;
import jakarta.persistence.OneToMany;


@SuppressWarnings("serial")
@Entity
public class Driver extends Usuario  implements Serializable{
	@OneToMany(mappedBy = "driver",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.EAGER)
	private List<Ride> rides = new ArrayList<>();

	public Driver() {}

	public Driver(String email, String name, String password) {
		super(email, name, password);
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public String toString(){
		return super.getEmail()+";"+this.rides;
	}

	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param from the origin location 
	 * @param to the destination location 
	 * @param date the date of the ride 
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesRideExists(String from, String to, Date date)  {	
		for (Ride r:rides)
			if ( (java.util.Objects.equals(r.getFrom(),from)) && (java.util.Objects.equals(r.getTo(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
				return true;

		return false;
	}
}

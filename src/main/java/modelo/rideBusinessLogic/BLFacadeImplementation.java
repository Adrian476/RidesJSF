package modelo.rideBusinessLogic;
import java.util.Date;
import java.util.List;
import modelo.rideDominio.Ride;
import modelo.ridePrincipal.HibernateDataAccess;
import modelo.rideExceptions.*;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation  implements BLFacade {
	HibernateDataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager = new HibernateDataAccess();
		    dbManager.initializeDB();

		
	}
	
    public BLFacadeImplementation(HibernateDataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
//		ConfigXML c=ConfigXML.getInstance();
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
     public List<String> getDepartCities(){
		List<String> departLocations=dbManager.getDepartCities();	
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	 public List<String> getDestinationCities(String from){
		List<String> targetCities=dbManager.getArrivalCities(from);		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 * @throws modelo.rideExceptions.RideAlreadyExistException 
	 * @throws modelo.rideExceptions.RideMustBeLaterThanTodayException 
	 * 
	 */
   
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideAlreadyExistException, RideMustBeLaterThanTodayException{
	   
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);		
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */
	 
	public List<Ride> getRides(String from, String to, Date date){
		List<Ride>  rides=dbManager.getRides(from, to, date);
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		return dates;
	}

}


package modelo.rideBusinessLogic;
import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import modelo.JPAUtil;
import modelo.rideDominio.Driver;
import modelo.rideDominio.Ride;
import modelo.ridePrincipal.HibernateDataAccess;
import modelo.rideExceptions.*;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation  implements BLFacade {
	HibernateDataAccess dbManager;

	public BLFacadeImplementation() {
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager = new HibernateDataAccess();

		// Condicional: Solo inicializa si NO hay datos (check si hay drivers o rides)
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Long> query = em.createQuery("SELECT COUNT(d) FROM Driver d", Long.class);
			long count = query.getSingleResult();
			if (count == 0) {  //BD vacía
				dbManager.initializeDB();
				System.out.println("BD inicializada por primera vez");
			} else {
				System.out.println("BD ya inicializada, skip");
			}
		} finally {
			em.close();
		}
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

	/**
	 * {@inheritDoc}
	 * @throws IncorrectPasswordException 
	 */
	public Driver logIn(String mail, String password) throws IncorrectPasswordException {
		return dbManager.logIn(mail, password);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Driver registerDriver(String mail, String name, String password) {
	    return dbManager.registerDriver(mail, name, password);
	}



}


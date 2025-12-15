package modelo.DataAccess;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import configuration.UtilDate;
import jakarta.persistence.EntityManager;
import modelo.JPAUtil;
import jakarta.persistence.*;
import modelo.rideDominio.Driver;
import modelo.rideDominio.Ride;
import modelo.rideExceptions.IncorrectPasswordException;
import modelo.rideExceptions.RideAlreadyExistException;
import modelo.rideExceptions.RideMustBeLaterThanTodayException;
public class HibernateDataAccess {

	public HibernateDataAccess() {}

	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	public List<String> getDepartCities(){
		EntityManager em = JPAUtil.getEntityManager(); 
		System.out.println("=> getDepartCities");
		TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.departureCity FROM Ride r ORDER BY r.departureCity", String.class);
		List<String> cities = query.getResultList();
		System.out.println(cities.toString());
		return cities;

	}

	public List<String> getArrivalCities(String from){
		EntityManager em = JPAUtil.getEntityManager(); 
		System.out.println("=> getDepartCities from "+ from);
		TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.arrivalCity FROM Ride r WHERE r.departureCity=?1 ORDER BY r.arrivalCity",String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList(); 
		System.out.println(arrivingCities.toString());
		return arrivingCities;

	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws  RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= "+from+" to= "+to+" driver="+driverEmail+" date "+date);
		EntityManager em = JPAUtil.getEntityManager(); 
		Ride ride = null;
		try {
			if(new Date().compareTo(date)>0) {
				throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}

			em.getTransaction().begin();
			Driver driver = em.find(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				em.getTransaction().commit();
				throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			//next instruction can be obviated
			ride = new Ride(from, to, date, nPlaces, price, driver);
			driver.getRides().add(ride);
			em.persist(driver); 
			em.getTransaction().commit();

			System.out.println("Ride successfully created and persisted to the DB =>  from= "+ride.getFrom() +" to= "+ride.getTo()+" driver ="+ride.getDriver().getEmail()+" date =  "+ride.getDate());
			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			em.getTransaction().commit();


			return null;
		}
	}

	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> HibernateDataAccess: getRides=> from= "+from+" to= "+to+" date "+date);
		EntityManager em = JPAUtil.getEntityManager(); 
		List<Ride> res = new ArrayList<Ride>();	
		TypedQuery<Ride> query = em.createQuery("SELECT r FROM Ride r WHERE r.departureCity=?1 AND r.arrivalCity=?2 AND r.date=?3",Ride.class);   
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
		for (Ride ride:rides){
			res.add(ride);
		}
		System.out.println("Retrieved rides from the DB => " + res.toString());
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		EntityManager em = JPAUtil.getEntityManager(); 
		List<Date> res = new ArrayList<Date>();	

		Date firstDayMonthDate= UtilDate.firstDayMonth(date);
		Date lastDayMonthDate= UtilDate.lastDayMonth(date);


		TypedQuery<Date> query = em.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class);   

		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		for (Date d:dates){
			res.add(d);
		}
		return res;
	}

	public Driver logIn(String mail, String password) throws IncorrectPasswordException {
		System.out.println("HibernateDataAccess logIn => mail: " + mail);
		if (mail == null || mail.trim().isEmpty() || password == null || password.isEmpty()) { //JSF ya lo comprueba igualmente
			throw new IllegalArgumentException("Email y contraseña son obligatorios");
		}
		EntityManager em = JPAUtil.getEntityManager();
		Driver driver = em.find(Driver.class, mail);
		if (driver == null) return null;
		if (!driver.getPassword().equals(password)) throw new IncorrectPasswordException("Contraseña no válida");
		return driver;
	}
	
	public Driver registerDriver(String mail, String name, String password) {
		System.out.println("Hibernate DataAccess => registerDriver(" + mail + ", " + name + ")");
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        em.getTransaction().begin();
	        Driver existing = em.find(Driver.class, mail);
	        if (existing != null) {
	            em.getTransaction().rollback();
	            return null; 
	        }

	        Driver newDriver = new Driver(mail, name, password);
	        em.persist(newDriver);

	        em.getTransaction().commit();
	        System.out.println("Driver registrado correctamente: " + mail);
	        return newDriver;
	    } catch (Exception e) {
	        if (em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        e.printStackTrace();
	        throw new RuntimeException("Error al registrar el driver", e);
	    }
	}

	public Driver getDriverByEmail(String email) {
	    EntityManager em = JPAUtil.getEntityManager();
	    return em.find(Driver.class, email);
	}

	public void initializeDB() {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        em.getTransaction().begin();

	        Driver driver1 = em.find(Driver.class, "driver@test.com");
	        if (driver1 == null) {
	            driver1 = new Driver("driver@test.com", "Test Driver", "test123");
	        }

	        Driver driver2 = em.find(Driver.class, "ana@test.com");
	        if (driver2 == null) {
	            driver2 = new Driver("ana@test.com", "Ana García", "test123");
	        }

	        Driver driver3 = em.find(Driver.class, "jose@test.com");
	        if (driver3 == null) {
	            driver3 = new Driver("jose@test.com", "José López", "test123");
	        }

	        Date fecha = UtilDate.newDate(2025, 11, 25);

	        Ride ride1 = new Ride("Bilbao", "Madrid", fecha, 4, 25.5f, driver1);
	        Ride ride2 = new Ride("Getxo", "Portugalete", fecha, 4, 25.5f, driver1);
	        Ride ride3 = new Ride("Bilbao", "Donostia", fecha, 4, 25.5f, driver1);
	        Ride ride4 = new Ride("Bilbao", "Vitoria", fecha, 5, 20.0f, driver1);
	        Ride ride5 = new Ride("Donostia", "Irun", fecha, 3, 15.0f, driver1);

	        driver1.getRides().add(ride1);
	        driver1.getRides().add(ride2);
	        driver1.getRides().add(ride3);
	        driver1.getRides().add(ride4);
	        driver1.getRides().add(ride5);

	        Ride ride6 = new Ride("Madrid", "Barcelona", fecha, 4, 45.0f, driver2);
	        Ride ride7 = new Ride("Barcelona", "Valencia", fecha, 3, 35.0f, driver2);
	        Ride ride8 = new Ride("Madrid", "Sevilla", fecha, 6, 55.0f, driver2);
	        Ride ride9 = new Ride("Valencia", "Alicante", fecha, 2, 18.0f, driver2);

	        driver2.getRides().add(ride6);
	        driver2.getRides().add(ride7);
	        driver2.getRides().add(ride8);
	        driver2.getRides().add(ride9);

	        Ride ride10 = new Ride("Barcelona", "Bilbao", fecha, 5, 60.0f, driver3);
	        Ride ride11 = new Ride("Vitoria", "Logroño", fecha, 4, 22.0f, driver3);
	        Ride ride12 = new Ride("Pamplona", "Zaragoza", fecha, 3, 28.0f, driver3);
	        Ride ride13 = new Ride("Zaragoza", "Madrid", fecha, 4, 40.0f, driver3);

	        driver3.getRides().add(ride10);
	        driver3.getRides().add(ride11);
	        driver3.getRides().add(ride12);
	        driver3.getRides().add(ride13);

	        em.persist(driver1);
	        em.persist(driver2);
	        em.persist(driver3);

	        em.getTransaction().commit();
	        System.out.println("Base de datos inicializada con datos de prueba");
	    } catch (Exception e) {
	        if (em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	}


}

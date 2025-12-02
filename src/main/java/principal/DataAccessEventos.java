package principal;

import modelo.JPAUtil;
import modelo.dominio.Usuario;
import modelo.dominio.EventoLogin;
import modelo.dominio.Maquina;
import modelo.dominio.Persona;

import java.util.*;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
public class DataAccessEventos {

	public DataAccessEventos (){}

	public Usuario createAndStoreUsuario(String nombre, String password,
			String tipo) {
		EntityManager em = JPAUtil.getEntityManager();
		Usuario u = new Usuario();
		try {
			em.getTransaction().begin();
			u.setNombre(nombre);
			u.setPassword(password);
			u.setTipo(tipo);
			em.persist(u);;
			em.getTransaction().commit();


		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
		return u;

	}

	public EventoLogin createAndStoreEventoLogin(String usuario, boolean login,
			Date fecha) {
		EntityManager em = JPAUtil.getEntityManager();
		EventoLogin e = new EventoLogin();

		try {
			em.getTransaction().begin();

			e.setUsuario (((Usuario)em.find(Usuario.class, usuario)));
			e.setLogin(login);
			//if (fecha!=null) {
				e.setFecha(fecha);
			//}
			//else throw new Exception("Falta la fecha");
			em.persist(e);
			em.getTransaction().commit();

		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			System.out.println("═══════════════════════════════════════");
			System.out.println("ERROR AL PERSISTIR:");
			System.out.println("Tipo de excepción: " + ex.getClass().getName());
			System.out.println("Mensaje: " + ex.getMessage());
			System.out.println("═══════════════════════════════════════");
			ex.printStackTrace(); // Ver la traza completa
			
			e = null; 
			
		} finally {
			em.close();
		}
		return e;
	}

	public boolean deleteUsuario(String usuario) {
		EntityManager em = JPAUtil.getEntityManager();

		try {
			// TRANSACCIÓN 1: Borrar eventos
			em.getTransaction().begin();
			Query query = em.createQuery(
					"DELETE FROM EventoLogin e WHERE e.usuario.nombre = :nombreUsuario");
			query.setParameter("nombreUsuario", usuario);
			int deletedCount = query.executeUpdate();
			em.getTransaction().commit();
			System.out.println("Eventos borrados: " + deletedCount);

			// Cerrar y abrir nuevo EntityManager
			em.close();
			
			/*
			em = JPAUtil.getEntityManager();

			// TRANSACCIÓN 2: Borrar usuario
			em.getTransaction().begin();
			Usuario u = em.find(Usuario.class, usuario);
			if (u == null) {
				System.out.println("Usuario no existe: " + usuario);
				em.getTransaction().rollback();
				return false;
			}
			em.remove(u);
			em.getTransaction().commit();
			System.out.println("Usuario borrado: " + usuario);
			*/
			
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
		return true;
	} 


	public Usuario createAndStoreUsuarioConEventoLogin(String nombre, String password, String tipo,
			boolean login, Date fecha) {

		EntityManager em = JPAUtil.getEntityManager();
		Usuario u = new Usuario();
		EventoLogin lg = new EventoLogin();

		try {
			em.getTransaction().begin();
			u.setNombre(nombre);
			u.setPassword(password);
			u.setTipo(tipo);

			lg.setUsuario(u);
			lg.setLogin(login);
			lg.setFecha(fecha);

			Set<EventoLogin> eventos = new HashSet<EventoLogin>();
			eventos.add(lg);
			u.setEventos(eventos);
			
			em.persist(u);
			//em.persist(lg); 
			em.getTransaction().commit();

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				System.out.println("Error: "+e.toString());
			}
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
		return u;
	}

	public Usuario getUsuario(String usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		Usuario result = null;
		try {
			em.getTransaction().begin();
			TypedQuery<Usuario> q = em.createQuery(
					"select u from Usuario u where u.nombre = :usuario", Usuario.class);
			q.setParameter("usuario", usuario);
			result = q.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Error al recuperar el usuario: " + e.getMessage());
		} finally {
			em.close();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		/*
		DataAccessEventos e = new DataAccessEventos ();

		//System.out.println("Creación de eventos:");
		//e.createAndStoreUsuario ("Ane", "125", "alumno");
		//e.createAndStoreEventoLogin("Ane",true, new Date());
		//e.createAndStoreEventoLogin("Ane",false, new Date());
		//e.createAndStoreUsuario("Kepa", "126", "alumno");
		//e.createAndStoreEventoLogin("Kepa",true, new Date());
		//e.createAndStoreEventoLogin("Kepa",false, new Date());
		//List <Usuario> us=e.getUsuarios();
		//System.out.println("Usuarios:" + us);
		//List <EventoLogin> lg=e.getEventosLogin();
		//System.out.println("EventosLogin: "+lg);
		//Usuario usua=lg.get(0).getUsuario();
		// lg.get(0) se obtiene el usuario a partir del evento: .getUsuario()
		//System.out.println(usua); 

		System.out.println("---------- PUNTO 3~ ---------");
		System.out.println("-----------------------------");
		//List <EventoLogin> lg1=e.getEventosLoginv1(usua.getNombre());
		//System.out.println("Eventos Login de: " + usua.getNombre()+": "+lg1);
		//List <EventoLogin> lg2=e.getEventosLoginv2(usua.getNombre());
		//System.out.println("Eventos Login de: " + usua.getNombre()+": "+lg2);
		//List <EventoLogin> lg3=e.getEventosLoginv3(usua.getNombre());
		//System.out.println("Eventos Login de: " + usua.getNombre()+": "+lg3); 

		//System.out.println(usua.getEventos());
		
		System.out.println("---------- PUNTO 4 ----------");
		System.out.println("-----------------------------");
		
		//EventoLogin lg3 = e.createAndStoreEventoLogin("Ane",true, null);
		//System.out.println(lg3); 

		//e.createAndStoreUsuario("Nekane", "127", "alumno");
		//e.createAndStoreEventoLogin("Nekane",true, new Date());
		//System.out.println(e.getEventosLogin());
		//boolean res=e.deleteUsuario ("Nekane");
		//System.out.println(e.getEventosLogin()); 
		
		Usuario usua= e.createAndStoreUsuarioConEventoLogin ("Peru","128","alumno",true,new Date());
		//System.out.println("=> " + e.getUsuarios());
		//System.out.println("=> " + e.getEventosLogin());
		//System.out.println("=> Usuario: " +usua+" Sus eventos de login: "+usua.getEventos()); 

		//System.out.println("=> Usuario: " + usua); // Será Peru
		//System.out.println("1=>"+usua.getEventos()); // el usuario no tendrá eventos de login
		//usua = e.getUsuario("Peru");
		//System.out.println("2=>"+usua.getEventos()); // ahora sí, se han traído de la base de datos!

		System.out.println("---------- PUNTO 5 ----------");
		System.out.println("-----------------------------");

		EntityManager em = JPAUtil.getEntityManager();
		EventoLogin lg5;
		try {
			em.getTransaction().begin();
			
			EntityGraph<?> graph = em.getEntityGraph("EventoLogin.conUsuario");
			Map<String, Object> pistas = new HashMap<>();
			pistas.put("javax.persistence.fetchgraph", graph); 
			
			System.out.println("5 => get(EventoLogin.class,1L) => ");
			lg5 = em.createQuery(
					 "SELECT DISTINCT e FROM EventoLogin e " +
					"LEFT JOIN FETCH e.usuario u " +
					"LEFT JOIN FETCH u.eventos " +
					"WHERE e.id = :id",
					 EventoLogin.class)
					 .setParameter("id", 1L)
					 .getSingleResult(); 
			System.out.println("5 => getUsuario().getTipo() => ");
			System.out.println(lg5.getUsuario().getTipo());
			em.getTransaction().commit();

		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Error: " + ex.getMessage());
		} finally {
			em.close();
		} 
		 */

		
		System.out.println("------PUNTO 6------");
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Usuario u1=new Usuario();
			u1.setNombre("Kepa");
			u1.setPassword("125");
			u1.setTipo("alumno");
			Usuario u2=new Usuario();
			u2.setNombre("Nerea");
			u2.setPassword("126");
			u2.setTipo("alumno");

			Maquina m1=new Maquina();
			m1.setCodigo(1);
			m1.setNombre("Casa");

			Maquina m2=new Maquina();
			m2.setCodigo(2);
			m2.setNombre("Trabajo");

			Set<Maquina> ms=new HashSet<Maquina>();
			ms.add(m1);
			ms.add(m2);

			Set<Usuario> us=new HashSet<Usuario>();
			us.add(u1);
			us.add(u2);

			u1.setMaquinas(ms);
			u2.setMaquinas(ms);

			m1.setUsuarios(us);
			m2.setUsuarios(us);
			em.persist(u1);
			em.persist(u2);
			em.persist(m1);
			em.persist(m2);

			em.getTransaction().commit();

		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Error: " + ex.getMessage());
		} finally {
			em.close();
		} 
		

		/*
		System.out.println("-----PUNTO 7-----");
		EntityManager em = JPAUtil.getEntityManager();
		Usuario u3 = new Usuario();
		try {
			em.getTransaction().begin();

			u3.setNombre("Koldo");
			u3.setPassword("125");
			u3.setTipo("alumno");
			Persona p1 = new Persona();
			p1.setTelefono("943112233");

			u3.setPersona(p1);
			p1.setUsuario(u3);

			em.persist(u3);
			em.persist(p1);

			em.getTransaction().commit();


		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Error : " + ex.getMessage());
		} finally {
			em.close();
		}

		System.out.println("P:<"+u3.getPersona()+"> U:<"+ u3.getPersona().getUsuario()+">");
		
	}

	public List<EventoLogin> getEventosLogin() {

		EntityManager em = JPAUtil.getEntityManager();
		List<EventoLogin> result = new ArrayList<EventoLogin>();
		try {
			em.getTransaction().begin();
			result = em.createQuery("from EventoLogin", EventoLogin.class).getResultList();
			//	System.out.println("getEventosLogin() : "+result);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return result;
	}

	public List<Usuario> getUsuarios() {
		EntityManager em = JPAUtil.getEntityManager();
		List<Usuario> result = new ArrayList<Usuario>();
		try {
			em.getTransaction().begin();
			result = em.createQuery("from Usuario", Usuario.class).getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return result;
		
		*/
	} 

	public List<EventoLogin> getEventosLoginv1(String nombreUsuario) {

		EntityManager em = JPAUtil.getEntityManager();
		List<EventoLogin> result = new ArrayList<EventoLogin>();
		try {
			em.getTransaction().begin();
			TypedQuery<EventoLogin> q = em.createQuery("select lg from EventoLogin lg inner join lg.usuario u where u.nombre= :nombreUsuario", EventoLogin.class);
			q.setParameter("nombreUsuario", nombreUsuario);
			result = q.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return result;
	} 

	public List<EventoLogin> getEventosLoginv2(String nombreUsuario) {

		EntityManager em = JPAUtil.getEntityManager();
		List<EventoLogin> result = new ArrayList<EventoLogin>();
		try {
			em.getTransaction().begin();
			TypedQuery<EventoLogin> q = em.createQuery("select lg from EventoLogin lg where lg.usuario.nombre= :nombreUsuario", EventoLogin.class);
			q.setParameter("nombreUsuario", nombreUsuario);
			result = q.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return result;
	}

	public List<EventoLogin> getEventosLoginv3(String nombreUsuario) {
		EntityManager em = JPAUtil.getEntityManager();
		List<EventoLogin> result = new ArrayList<>();

		try {
			em.getTransaction().begin();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventoLogin> query = cb.createQuery(EventoLogin.class);
			Root<EventoLogin> eventoLogin = query.from(EventoLogin.class);
			Join<EventoLogin, Usuario> usuario = eventoLogin.join("usuario");
			query.select(eventoLogin)
			.where(cb.equal(usuario.get("nombre"), nombreUsuario));

			
			
			result = em.createQuery(query).getResultList();
			em.getTransaction().commit();

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
		
		return result;
	} 
} 

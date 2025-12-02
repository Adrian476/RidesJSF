package principal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import modelo.JPAUtil;
import modelo.dominio.EventoLogin;
import modelo.dominio.Usuario;

import java.util.*;
public class CrearEventos {

	public CrearEventos() {
	}
	private void createAndStoreEventoLogin(Long id, String descripcion, Date fecha) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			EventoLogin e = new EventoLogin();
			e.setId(id);
			e.setDescripcion(descripcion);
			e.setFecha(fecha);
			em.persist(e);

			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}
	private List<EventoLogin> listaEventos() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<EventoLogin> result = em.createQuery("from EventoLogin",
					EventoLogin.class).getResultList();
			em.getTransaction().commit();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}
	
	private void createAndStoreEventoLogin(String descripcion, Date fecha) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			EventoLogin e = new EventoLogin();
			e.setDescripcion(descripcion);
			e.setFecha(fecha);
			em.persist(e);

			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}
	/*
	public static void main(String[] args) {
		CrearEventos e = new CrearEventos();

		System.out.println("Creación de eventos:");
		e.createAndStoreEventoLogin("Pepe ha hecho login correctamente", new Date());
		e.createAndStoreEventoLogin("Nerea ha intentado hacer login", new Date());
		e.createAndStoreEventoLogin("Kepa ha hecho login correctamente", new Date());

		System.out.println("Listado de eventos:");
		List<EventoLogin> eventos = e.listaEventos();

		for (EventoLogin ev : eventos) {
			System.out.println("Id: " + ev.getId() + " Descripcion: "
					+ ev.getDescripcion() + " Fecha: " + ev.getFecha());
		}
	}
	
	
	public static void main(String[] args) {
		CrearEventos e = new CrearEventos();

		System.out.println("Creación de eventos:");
		e.createAndStoreUsuario("Ane", "125", "alumno");
		e.createAndStoreEventoLogin ("Ane",true, new Date());
		e.createAndStoreEventoLogin ("Ane",false, new Date());

		System.out.println("Lista de eventos:");

		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			List<EventoLogin> result = em.createQuery("from EventoLogin",
					EventoLogin.class).getResultList();

			for (int i = 0; i < result.size(); i++) {
				EventoLogin ev = (EventoLogin) result.get(i);
				System.out.println("Id: " + ev.getId() + " Descripción: " +
						ev.getDescripcion() + " Fecha: " + ev.getFecha()+ " Login: " + ev.isLogin());
			}
			em.getTransaction().commit();

		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		}
		finally {
			em.close();
		}
	}
	*/
	public static void main(String[] args) {
		CrearEventos e = new CrearEventos();
		

		System.out.println("======================");
		System.out.println("Ciclo de vida de los objetos:");
		System.out.println("======================");
		Usuario u = new Usuario();
		u.setNombre("Nerea");
		u.setPassword("1234");
		u.setTipo("profesor");
		System.out.println("new => TRANSIENT");
		e.printObjMemBD("Nerea está en memoria, pero no en la BD ",u);

		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(u);
			System.out.println("save => MANAGED");
			e.printObjMemBD("Nerea todavía no está en la BD porque no se ha hecho commit",u);
			u.setPassword("1235");
			e.printObjMemBD("Se ha ejecutado u.setPassword(\"1235\") pero no commit. Por tanto, Nerea todavía no está en la BD.",u);
			em.getTransaction().commit();
			System.out.println("close (commit) => DETACHED");
			e.printObjMemBD("Se ha hecho commit, los cambios se han hecho en la BD ",u);
			u.setPassword("1236");
			e.printObjMemBD("Se ha ejecutado u.setPassword(\"1236\"), pero el objeto no está conectado con la BD (detached)",u);

		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
		em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(u);
			em.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println("save => ERROR: como el objeto está 'detached', save intenta meter el mismo objeto de nuevo y saltará un error de clave primaria.");
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		} finally {
			em.close();
		}

		em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(u);
			System.out.println("merge => MANAGED");
			e.printObjMemBD("ahora el objeto es gestionado, pero para poder ver la nueva contraseña, habrá que hacer commit",u);
			em.getTransaction().commit();
			System.out.println("close (commit) => DETACHED");
			e.printObjMemBD("\tSe ha hecho commit, se ve la nueva contraseña en la base de	datos",u);
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
		u.setPassword("1237");
		e.printObjMemBD("Se ha ejecutado u.setPassword(\"1237\"), pero el objeto no está conectado a la base de datos (detached)",u);

		em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			u = em.find(Usuario.class, u.getNombre());
			System.out.println("find => MANAGED");
			e.printObjMemBD("Se ha vuelto a recuperar el usuario de la base de datos, por tanto, se ha perdido el cambio de la contraseña",u);
			em.remove(u);
			System.out.println("remove => REMOVED");
			e.printObjMemBD("Hasta que se haga commit no se borrará el objeto de la base de datos ",u);
			em.getTransaction().commit();
			System.out.println("close (commit) => DETACHED");
			e.printObjMemBD("Se ha hecho commit, no está en la base de datos pero sí en memoria.",u);

		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally { 
			em.close();
		} 
	}

	private void createAndStoreUsuario(String nombre, String password, String tipo) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Usuario u = new Usuario();
			u.setNombre(nombre);
			u.setPassword(password);
			u.setTipo(tipo);
			em.persist(u);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	} 

	private void createAndStoreEventoLogin(String usuario, boolean login, Date fecha) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			TypedQuery<Usuario> q = em.createQuery("SELECT u FROM Usuario u WHERE u.nombre = :usuario", Usuario.class);
					q.setParameter("usuario", usuario);
					List<Usuario> result = q.getResultList();

					if (!result.isEmpty()) {
						EventoLogin e = new EventoLogin();
						e.setUsuario(result.get(0));
						e.setLogin(login);
						e.setFecha(fecha);
						em.persist(e);
						em.getTransaction().commit();
					} else {
						System.out.println("Error al crear instancia de EventoLogin: no existe usuario "
								+ usuario);
						em.getTransaction().rollback();
					}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	} 
	

	public void printObjMemBD(String desc, Usuario u) {
		System.out.print("\tMem:<"+u+"> DB:<"+VerEventosUsandoJDBC.getUsuarioJDBC(u)+"> =>");
		System.out.println(desc); 
	} 
} 


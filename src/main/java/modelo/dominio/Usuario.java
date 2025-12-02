package modelo.dominio;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
@Entity
public class Usuario {
	@Id
	private String nombre;
	private String password;
	private String tipo;
	@OneToMany(targetEntity=EventoLogin.class, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy="usuario", fetch=FetchType.LAZY)//cascade=CascadeType.REMOVE
	private Set<EventoLogin> eventos;
	@ManyToMany (mappedBy="usuarios")
	private Set<Maquina> maquinas;
	@OneToOne
	private Persona persona;

	public Usuario(){}

	public Persona getPersona() {
		return persona;
	}
	
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public Set<Maquina> getMaquinas() {
		return maquinas;
	}

	public void setMaquinas(Set<Maquina> maquinas) {
		this.maquinas = maquinas;
	}

	public Set<EventoLogin> getEventos() {
		return eventos;
	}
	
	public void setEventos(Set<EventoLogin> eventos) {
		this.eventos = eventos;
	}

	public String getTipo() {
		return tipo; 
	}

	public void setTipo(String tipo) {
		this.tipo = tipo; 
	}

	public String getNombre() {
		return nombre; 
	}

	public void setNombre(String nombre) {
		this.nombre = nombre; 
	}

	public String getPassword() {
		return password; 
	}

	public void setPassword(String password) {
		this.password = password; 
	}
	
	public String toString() { // Usuario
		return nombre+"/"+password+"/"+tipo;
	}
}

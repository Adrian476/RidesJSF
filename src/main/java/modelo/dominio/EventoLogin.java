package modelo.dominio;

import java.util.Date;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@NamedEntityGraph(
		name = "EventoLogin.conUsuario",
		attributeNodes = @NamedAttributeNode("usuario")
) 

@Entity

public class EventoLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descripcion;
	@Column(nullable = false)
	private Date fecha;
	@ManyToOne(targetEntity=Usuario.class, cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	private Usuario usuario;
	private boolean login;

	public EventoLogin() {}

	public Long getId() {
		return id; 
	}

	public void setId(Long id) {
		this.id = id; 
	}

	public String getDescripcion() {
		return descripcion; 
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion; 	
	}

	public Date getFecha() {
		return fecha; 
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha; 
	}

	public Usuario getUsuario() {
		return usuario; 
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario; 
	}

	public boolean isLogin() {
		return login; 
	}

	public void setLogin(boolean login) {
		this.login = login;
		if (login)
			this.descripcion = usuario.getNombre() +
			" ha hecho login correctamente";
		else this.descripcion = usuario.getNombre() +
				" ha intentado hacer login"; 
	}
	
	public String toString() { // EventoLogin
		return id+"/"+descripcion+"/"+fecha+"/"+usuario+"/"+login;
	}
}
package modelo.dominio;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
@Entity
public class Persona {
	public Persona() {
	}
	@Id
	private int codigo;
	private String telefono;
	@OneToOne
	private Usuario usuario;
	public int getcodigo() {
		return codigo;}
	public void setCodigo(int codigo) {
		this.codigo = codigo; }
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String toString() {
		return codigo + "/" + telefono;
	}
} 


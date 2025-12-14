package modelo.rideDominio;
import jakarta.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Usuario implements Serializable {

	@Id
	private String email;
	@Column(nullable = false)
	private String password; 
	private String name;

	public Usuario() {}

	public Usuario(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}


	public String getEmail() { 
		return email; 
	}
	public void setEmail(String email) { 
		this.email = email; 
	}

	public String getPassword() { 
		return password;
	}
	public void setPassword(String password) { 
		this.password = password; 
	}

	public String getName() { 
		return name;
	}
	public void setName(String name) { 
		this.name = name; 
	}
}
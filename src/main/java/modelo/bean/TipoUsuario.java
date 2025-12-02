package modelo.bean;

public class TipoUsuario {
	public String tipoUsu;
	public int codigo;

	public TipoUsuario(int codigo, String tipoUsu){
		this.tipoUsu=tipoUsu;
		this.codigo=codigo;
	}
	public String getTipoUsu() {
		return tipoUsu;
	}
	public void setTipoUsu(String tipoUsu) {
		this.tipoUsu = tipoUsu;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String toString(){
		return tipoUsu;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
		return result; 
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoUsuario other = (TipoUsuario) obj;
		if (codigo != other.codigo)
			return false;
		return true; 
	}


}

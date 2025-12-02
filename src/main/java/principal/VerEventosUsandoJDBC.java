package principal;

import java.sql.*;
import java.util.Date;

import modelo.dominio.Usuario;
public class VerEventosUsandoJDBC {
	public static void main(String[] args) {
		Connection c;
		Statement s;
		ResultSet rs;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mariadb://localhost/eventos","root","admin");
			s = c.createStatement();
			rs = s.executeQuery("SELECT * FROM EVENTOLOGIN");
			System.out.println("EVENTOLOGIN (ID, DESCRIPCION, FECHA");
			while (rs.next()){
				Long id = rs.getLong("ID");
				String descripcion = rs.getString("DESCRIPCION");
				Date fecha = rs.getDate("FECHA");
				System.out.println(id+" / "+descripcion+" / "+fecha);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getUsuarioJDBC(Usuario u) {
		Connection c;
		PreparedStatement s;
		ResultSet rs;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mariadb://localhost/eventos","root","admin");
			s = c.prepareStatement("SELECT * FROM USUARIO WHERE nombre=?");
			s.setString(1,u.getNombre());
			rs = s.executeQuery();
			if (rs.next()) {
				return (String)(rs.getString("NOMBRE")+"/"+rs.getString("PASSWORD")+"/"+rs.getString("TIPO"));
			}
		} catch (Exception ex) {ex.printStackTrace();}
		return "--/--/--";

	}
}
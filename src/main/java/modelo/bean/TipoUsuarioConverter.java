package modelo.bean;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter; 

@FacesConverter("TipoUsuarioConverter")
public class TipoUsuarioConverter implements Converter<TipoUsuario>{

	@Override
	public TipoUsuario getAsObject(FacesContext context, UIComponent
			component, String value)
					throws ConverterException {

		// String → Objeto TipoUsuario
		// Se ejecuta cuando el formulario envía datos al servidor
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		return LoginBean.getObject(value);
	}
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, TipoUsuario value) throws ConverterException {
		if (value==null)
			return "";
		return value.toString();
	}
	
	
	
}

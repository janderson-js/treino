package br.com.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.annotations.Immutable;

import br.com.JPAUseful.JPAUseful;
import br.com.entities.Cities;

@FacesConverter(forClass = Cities.class, value = "converterCities")
@Immutable
public class CitiesConverter implements  Converter {


	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String idCity) {
		Cities cities = new Cities();
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		try {
			
			if(!idCity.trim().equals("") && !idCity.isEmpty() && Long.parseLong(idCity) !=0) {
				cities = (Cities) em.find(Cities.class, Long.parseLong(idCity));
				return cities;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cities = null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object idCity) {
		
		if(idCity == null) {
			return null;
		}else if(idCity instanceof Cities){
			return ((Cities) idCity).getId().toString();
		}else {
			return idCity.toString();
		}
	}

}

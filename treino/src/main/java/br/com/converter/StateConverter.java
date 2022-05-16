package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.JPAUseful.JPAUseful;
import br.com.entities.States;
@FacesConverter(forClass = States.class, value = "converterStates")
public class StateConverter implements Serializable, Converter {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String idState) {
		States state = new States();
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		try {
			
			if(!idState.trim().equals("") && !idState.isEmpty() && Long.parseLong(idState) !=0) {
				state = (States) em.find(States.class, Long.parseLong(idState));
				return state;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return state = null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object state) {
		
		if(state == null) {
			return null;
		}else if(state instanceof States) {
			return ((States) state).getId().toString();
		}else {
			return state.toString();
		}
	}


}

package br.com.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.JPAUseful.JPAUseful;
import br.com.entities.Person;
import br.com.entities.States;

public class IDAOPersonImpl implements IDAOPerson, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Person consultUser(String login, String password) {

		Person person = null;
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		person = (Person) em.createQuery("Select p from Person p where p.login='"+login+"' and p.password='"+password+"'").getSingleResult();
		
		et.commit();
		em.close();
		
		return person;
	}

	@Override
	public List<SelectItem> listStates() {
		List<SelectItem> selectStates = new ArrayList<SelectItem>();
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		List<States> states = em.createQuery("FROM States ORDER BY NOME").getResultList();
		
		et.commit();
		em.close();
		
		for (States st : states) {
			selectStates.add(new SelectItem(st, st.getName()));
		}
		
		return selectStates;
	}

}

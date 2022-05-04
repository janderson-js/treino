package br.com.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.JPAUseful.JPAUseful;
import br.com.entities.Person;

public class IDAOPersonImpl implements IDAOPerson, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Person consultUser(String login, String password) {

		Person person = null;
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		person = (Person) em.createQuery("Select p from Person p where p.login='"+login+"' and p.password='"+password+"'");
		
		et.commit();
		em.close();
		
		return person;
	}

}

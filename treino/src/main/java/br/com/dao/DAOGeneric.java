package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.JPAUseful.JPAUseful;


public class DAOGeneric<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public void toSaveDAO(E entity) {
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		em.persist(entity);
		
		et.commit();
		em.close();		
	}
	
	public E toMergeDAO(E entity) {
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		E person = em.merge(entity);
		
		et.commit();
		em.close();	
		
		return person;
	}
	
	public List<E> listPeopleDAO(E entity){
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		List<E> people =  em.createQuery("from "+ entity.getClass().getCanonicalName() +" order by name").getResultList();
		
		et.commit();
		em.close();
		
		return  people;
	}
	
	public void toDeleteDAO(E entity) {
		
		EntityManager em = JPAUseful.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		Object id = JPAUseful.getPrimaryKey(entity);
		em.createQuery("Delete from "+entity.getClass().getCanonicalName()+ " where id="+ id).executeUpdate();
		
		et.commit();
		em.close();
		
	}

}

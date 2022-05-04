package br.com.JPAUseful;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUseful implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static EntityManagerFactory emf;
	
	static {
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("treino");
		}
	}
	
	public static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
	
	public static Object getPrimaryKey(Object entities) {
		
		return emf.getPersistenceUnitUtil().getIdentifier(entities);
	}

}

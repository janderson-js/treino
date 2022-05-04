package br.com.repository;

import br.com.entities.Person;

public interface IDAOPerson {
	
	Person consultUser(String login, String password);
	
}

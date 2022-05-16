package br.com.repository;

import java.util.List;

import javax.faces.model.SelectItem;

import br.com.entities.Person;

public interface IDAOPerson {
	
	Person consultUser(String login, String password);
	
	List<SelectItem> listStates();
	
}

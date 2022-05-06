package br.com.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.dao.DAOGeneric;
import br.com.entities.Person;
import br.com.repository.IDAOPerson;
import br.com.repository.IDAOPersonImpl;

@ManagedBean(name = "personBean")
@ViewScoped
public class PersonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Person person = new Person();
	
	private List<Person> people = new ArrayList<Person>();
	
	private DAOGeneric<Person> daoGeneric = new DAOGeneric<Person>();
	
	private IDAOPerson idaoPerson = new IDAOPersonImpl();
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	  
	public DAOGeneric<Person> getDaoGeneric() {
		return daoGeneric;
	}
	public void setDaoGeneric(DAOGeneric<Person> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	public IDAOPerson getIdaoPerson() {
		return idaoPerson;
	}
	public void setIdaoPerson(IDAOPerson idaoPerson) {
		this.idaoPerson = idaoPerson;
	}
	public List<Person> getPeople() {
		return people;
	}
	public void setPeople(List<Person> people) {
		this.people = people;
	}

	public String Login() {
		
		Person userLoaded = new Person();
		
		if(idaoPerson.consultUser(person.getUserLogin(), person.getUserPassword()) != null) {
			userLoaded = idaoPerson.consultUser(person.getUserLogin(), person.getUserPassword());
		}
		
		if(person != null) {
			
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			
			HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
			HttpSession session = req.getSession();
			
			session.setAttribute("userLoaded", userLoaded);
			
			return "paginajsf.jsf";
		}
		return "index.jsf";
	}
	
	public void messages(String message, String labelName) {
		FacesContext.getCurrentInstance().addMessage(labelName, new FacesMessage(message));
	}
	
	public String toClear() {
		person = new Person();
		
		return "";
	}
	
	
	public String toSave() throws IOException {
		
		try {
			if(person.getId() != null) {
				messages("Editado com sucesso!!", "formPersonMsg:toSuccessMessage");
			}else {
				messages("Cadastrado com sucesso!!", "formPersonMsg:toSuccessMessage");
			}
			person = daoGeneric.toMergeDAO(person); 
			listPerson();
		} catch (Exception e) {
			e.printStackTrace();
			if(person.getId() != null) {
				messages("Ocorreu um erro ao editar o usúario!!","formPerson:toDangerMessage");
			}else {
				messages("Ocorreu um erro ao cadastra o usúario!!","formPerson:toDangerMessage");
			}
			listPerson();
		}
		
		return "";
	}
	@PostConstruct
	public void listPerson() {
		people = daoGeneric.listPeopleDAO(person);
	}
	
	public Person toEdit() {
		return person;
	}
	
	public String toDelete() {
		
		try {
			daoGeneric.toDeleteDAO(person);
			toClear();
			listPerson();
			messages("Deletado com sucesso", "formPersonMsg:toSuccessMessage");
		} catch (Exception e) {
			e.printStackTrace();
			messages("Deletado com sucesso", "formPersonMsg:toDangerMessage");
		}
		
		return "";
	}
}

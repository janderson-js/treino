package br.com.bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.annotations.Immutable;

import com.google.gson.Gson;

import br.com.JPAUseful.JPAUseful;
import br.com.dao.DAOGeneric;
import br.com.entities.Cities;
import br.com.entities.Person;
import br.com.entities.States;
import br.com.repository.IDAOPerson;
import br.com.repository.IDAOPersonImpl;

@ManagedBean(name = "personBean")
@ViewScoped
@Immutable
public class PersonBean  {
	
	private Person person = new Person();
	
	private List<Person> people = new ArrayList<Person>();
	
	private DAOGeneric<Person> daoGeneric = new DAOGeneric<Person>();
	
	private IDAOPerson idaoPerson = new IDAOPersonImpl();
	
	private List<SelectItem> states;
	
	private List<SelectItem> cities;

	private Part photofile;
	
	public Part getPhotofile() {
		return photofile;
	}
	public void setPhotofile(Part photofile) {
		this.photofile = photofile;
	}
	
	public List<SelectItem> getCities() {
		return cities;
	}
	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}
	public List<SelectItem> getStates() {
		states = idaoPerson.listStates();
		return states;
	}
	public void setStates(List<SelectItem> states) {
		this.states = states;
	}
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
	
	public void searchCEP(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/"+ person.getCep() +"/json/");
			
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();
			
			while ((cep = br.readLine()) != null) {
				jsonCEP.append(cep);
			}
			
			Person gsonAux = new Gson().fromJson(jsonCEP.toString(), Person.class);
			
			person.setCep(gsonAux.getCep());
			person.setAddress(gsonAux.getAddress());
			person.setComplement(gsonAux.getComplement());
			person.setDistrict(gsonAux.getDistrict());
			person.setLocality(gsonAux.getLocality());
			person.setUf(gsonAux.getUf());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String Logout() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		
		HttpServletRequest req = (HttpServletRequest) context.getCurrentInstance().getExternalContext().
				getRequest();
		req.getSession().invalidate();
		
		return "index.jsf";
	}

	public String Login() {
		try {
			
			Person userLoaded = new Person();
			
			if(idaoPerson.consultUser(person.getLogin(), person.getPassword()) != null) {
				userLoaded = idaoPerson.consultUser(person.getLogin(), person.getPassword());
			}else {
				messages("Login ou senha invalido", "formIndexMsg:toDangerMessage");
			}
			
			if(userLoaded.getName() != null) {
				
				FacesContext context = FacesContext.getCurrentInstance();
				ExternalContext externalContext = context.getExternalContext();
				
				HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
				HttpSession session = req.getSession();
				
				session.setAttribute("userLoaded", userLoaded);
				
				return "paginajsf.jsf";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			messages("teste", "formIndexMsg:toDangerMessage");
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
		System.out.println(person.toString());
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
	

	public void loadingCities(AjaxBehaviorEvent event) {
		
		States state = (States) ((HtmlSelectOneMenu) event.getSource()).getValue();
		
		if(state != null) {
			person.setState(state);
			
			List<Cities> cities = JPAUseful.getEntityManager().createQuery("FROM Cities where states_id="+ state.getId()).getResultList();
			
			List<SelectItem> selectCities = new ArrayList<SelectItem>();
			
			for (Cities city : cities) {
				selectCities.add(new SelectItem(city, city.getNome()));
			}
			
			setCities(selectCities);
		}
		
	}
	
	public void setImage(AjaxBehaviorEvent event) throws IOException {
		byte[] imageByte = getByte(photofile.getInputStream());
		person.setPhotoIconBase64Original(imageByte);
		
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageByte));
		
		int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		
		int height = 200;
		int width = 200;
		
		BufferedImage resizedImage = new BufferedImage(height,width,type);
		Graphics2D g = resizedImage.createGraphics();
		
		g.drawImage(bufferedImage,0,0,width, height,null);
		g.dispose();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extension = photofile.getContentType().split("\\/")[1];
		ImageIO.write(resizedImage, extension, baos);
		
		String thumbnail = "data:" + photofile.getContentType()+";base64,"+ DatatypeConverter.printBase64Binary(baos.toByteArray());
		
		person.setPhotoBase64(thumbnail);
		person.setExtension(extension);
		
	}
	
	public void upload(Part file) throws IOException {
		
		this.photofile = (Part) photofile.getInputStream();
	}
	
	public byte[] getByte(InputStream is) throws IOException{
		
		int len;
		int size = 1024;
		byte[] buf = null;
		
		if(is instanceof ByteArrayInputStream){
			size = is.available();
			buf = new byte[size];
			len = is.read(buf,0,size);
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			
			while((len = is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0,len);
			}
			
			buf = bos.toByteArray();		
		}
		
		return buf;
	}
}

package br.com.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
@Entity
public class States implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	private String sigla;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	private Cities cities;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return nome;
	}
	public void setName(String name) {
		this.nome = name;
	}
	public String getInitials() {
		return sigla;
	}
	public void setInitials(String initials) {
		this.sigla = initials;
	}
	public Cities getCities() {
		return cities;
	}
	public void setCities(Cities cities) {
		this.cities = cities;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		States other = (States) obj;
		return Objects.equals(id, other.id);
	}
}

package br.com.teste;

import javax.persistence.Persistence;

public class TesteJPA {
	
	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("treino");
	}

}

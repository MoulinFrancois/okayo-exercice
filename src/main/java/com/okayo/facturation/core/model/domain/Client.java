package com.okayo.facturation.core.model.domain;

public class Client {
	
	private String code;
	private String nom;
	private Adresse adresse;
	
	public Client(String code, String nom, Adresse adresse) {
		this.code = code;
		this.nom = nom;
		this.adresse = adresse;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getNom() {
		return nom;
	}
	
	public Adresse getAdresse() {
		return adresse;
	}
	
}

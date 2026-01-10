package com.okayo.facturation.core.model.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Client {
	
	@NotBlank
	private String code;
	@NotBlank
	private String nom;
	@NotNull
	@Valid
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

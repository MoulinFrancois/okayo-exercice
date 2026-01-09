package com.okayo.facturation.core.model.domain;

public class Adresse {
	
	private String ligne;
	private String codePostal;
	private String ville;

	public Adresse(String ligne, String codePostal, String ville) {
		this.ligne = ligne;
		this.codePostal = codePostal;
		this.ville = ville;
	}

	public String getLigne() {
		return ligne;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public String getVille() {
		return ville;
	}
	
}

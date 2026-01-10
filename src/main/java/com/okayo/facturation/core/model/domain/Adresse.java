package com.okayo.facturation.core.model.domain;

import jakarta.validation.constraints.NotBlank;

public class Adresse {
	
	@NotBlank
	private String ligne;
	@NotBlank
	private String codePostal;
	@NotBlank
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

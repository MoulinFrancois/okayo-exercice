package com.okayo.facturation.core.model.tech;

import jakarta.validation.constraints.NotBlank;

public class ProduitRequest {

	@NotBlank
	private String label;

	public ProduitRequest() {
		super();
	}

	public ProduitRequest(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}

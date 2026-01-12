package com.okayo.facturation.core.model.tech;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProduitDataRequest {

	@NotNull
	private Integer produitId;
	@NotBlank
	private String designation;
	@NotNull
	private Double tva;
	@NotNull
	private Double prixUnitaireHT;

	public ProduitDataRequest() {
		super();
	}

	public ProduitDataRequest(Integer produitId, String designation, Double tva, Double prixUnitaireHT) {
		super();
		this.produitId = produitId;
		this.designation = designation;
		this.tva = tva;
		this.prixUnitaireHT = prixUnitaireHT;
	}

	public Integer getProduitId() {
		return produitId;
	}

	public void setProduitId(Integer produitId) {
		this.produitId = produitId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Double getTva() {
		return tva;
	}

	public void setTva(Double tva) {
		this.tva = tva;
	}

	public Double getPrixUnitaireHT() {
		return prixUnitaireHT;
	}

	public void setPrixUnitaireHT(Double prixUnitaireHT) {
		this.prixUnitaireHT = prixUnitaireHT;
	}

}

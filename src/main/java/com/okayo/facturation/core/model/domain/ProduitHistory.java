package com.okayo.facturation.core.model.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProduitHistory {

	private Integer id;
	@NotNull
	private Integer produitId;
	@NotBlank
	private String designation;
	@NotNull
	private Double tva;
	@NotNull
	private Double prixUnitaireHT;

	public ProduitHistory() {
		super();
	}

	public ProduitHistory(Integer id, Integer produitId, String designation, Double tva, Double prixUnitaireHT) {
		super();
		this.id = id;
		this.produitId = produitId;
		this.designation = designation;
		this.tva = tva;
		this.prixUnitaireHT = prixUnitaireHT;
	}

	public ProduitHistory(Integer produitId, String designation, Double tva, Double prixUnitaireHT) {
		this(null, produitId, designation, tva, prixUnitaireHT);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

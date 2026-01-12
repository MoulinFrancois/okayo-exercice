package com.okayo.facturation.core.model.domain;

import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProduitSouscrit {

	private Integer id;
	@NotNull
    private Integer produitId;
	@NotBlank
	private String clientCode;
	@NotNull
	@Min(value = 0)
	private Double quantite;
	private Date date;
	
	public ProduitSouscrit() {
		super();
	}

	public ProduitSouscrit(Integer id, Integer produitId, String clientCode, Double quantite, Date date) {
		super();
		this.id = id;
		this.produitId = produitId;
		this.clientCode = clientCode;
		this.quantite = quantite;
		this.date = date;
	}

	public ProduitSouscrit(Integer produitId, String clientCode, Double quantite, Date date) {
		super();
		this.produitId = produitId;
		this.clientCode = clientCode;
		this.quantite = quantite;
		this.date = date;
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

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Double getQuantite() {
		return quantite;
	}

	public void setQuantite(Double quantite) {
		this.quantite = quantite;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}

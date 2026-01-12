package com.okayo.facturation.core.model.tech;

import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProduitSouscritRequest {

	@NotNull
    private Integer produitId;
	@NotBlank
	private String clientCode;
	@NotNull
	@Min(value = 0)
	private Double quantite;
	
	public ProduitSouscritRequest() {
		super();
	}

	public ProduitSouscritRequest(Integer produitId, String clientCode, Double quantite) {
		super();
		this.produitId = produitId;
		this.clientCode = clientCode;
		this.quantite = quantite;
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

}

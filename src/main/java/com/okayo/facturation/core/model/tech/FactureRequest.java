package com.okayo.facturation.core.model.tech;

import java.util.Date;

public class FactureRequest {
	
	private String clientCode;
	private Date dateEcheance;
	
	public FactureRequest(String clientCode, Date dateEcheance) {
		this.clientCode = clientCode;
		this.dateEcheance = dateEcheance;
	}
	
	public String getClientCode() {
		return clientCode;
	}
	
	public Date getDateEcheance() {
		return dateEcheance;
	}
	
}

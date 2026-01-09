package com.okayo.facturation.core.model.domain;

import java.util.Date;
import java.util.List;

public class Facture {
	
	private String Reference;
	private Client client;
	private List<FacturationElement> elements;
	private Date dateFacturation;
	private Date dateEcheance;
	
	public Facture(String reference, Client client, List<FacturationElement> elements, Date dateFacturation, Date dateEcheance) {
		Reference = reference;
		this.client = client;
		this.elements = elements;
		this.dateFacturation = dateFacturation;
		this.dateEcheance = dateEcheance;
	}
	
	public String getReference() {
		return Reference;
	}
	
	public Client getClient() {
		return client;
	}
	
	public List<FacturationElement> getElements() {
		return elements;
	}
	
	public Date getDateFacturation() {
		return dateFacturation;
	}
	
	public Date getDateEcheance() {
		return dateEcheance;
	}
	
}

package com.okayo.facturation.core.model.domain;

import static java.util.stream.Collectors.toMap;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Facture {
	
	private String reference;
	private Client client;
	private List<FacturationElement> elements;
	private Date dateFacturation;
	private Date dateEcheance;
	private double totalHT;
	private double totalTTC;
	private Map<Double, Double> totalTVAParTaux;
	
	public Facture(String reference, Client client, List<FacturationElement> elements, Date dateFacturation, Date dateEcheance) {
		this.reference = reference;
		this.client = client;
		this.elements = elements;
		this.dateFacturation = dateFacturation;
		this.dateEcheance = dateEcheance;
		initializeTotals();
	}
	
	private void initializeTotals() {
		this.totalTVAParTaux = elements.stream().collect(toMap(
				FacturationElement::getTva,
				element -> element.getTva()/100d * element.getTotalHT(),
				Double::sum));
		this.totalHT = elements.stream().mapToDouble(FacturationElement::getTotalHT).sum();
		this.totalTTC = totalHT + totalTVAParTaux.values().stream().mapToDouble(Double::doubleValue).sum();
	}

	public String getReference() {
		return reference;
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

	public Map<Double, Double> getTotalTVAParTaux() {
		return totalTVAParTaux;
	}

	public double getTotalHT() {
		return totalHT;
	}

	public double getTotalTTC() {
		return totalTTC;
	}
	
}

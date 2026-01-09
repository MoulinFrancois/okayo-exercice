package com.okayo.facturation.core.model.domain;

public class FacturationElement {

	private String designation;
	private double tva;
	private double prixUnitaireHT;
	private int quantite;
	private double totalHT;
	
	public FacturationElement(String designation, double tva, double prixUnitaireHT, int quantite, double totalHT) {
		this.designation = designation;
		this.tva = tva;
		this.prixUnitaireHT = prixUnitaireHT;
		this.quantite = quantite;
		this.totalHT = totalHT;
	}
	
	public String getDesignation() {
		return designation;
	}
	
	public double getTva() {
		return tva;
	}
	
	public double getPrixUnitaireHT() {
		return prixUnitaireHT;
	}
	
	public int getQuantite() {
		return quantite;
	}
	
	public double getTotalHT() {
		return totalHT;
	}
		
}

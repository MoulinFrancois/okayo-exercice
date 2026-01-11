package com.okayo.facturation.core.model.domain;

public class Produit {

	private int id;
	private String label;

	public Produit() {
		super();
	}

	public Produit(String label) {
		super();
		this.label = label;
	}

	public Produit(int id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}

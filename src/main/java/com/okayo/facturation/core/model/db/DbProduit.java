package com.okayo.facturation.core.model.db;

import jakarta.persistence.*;

@Entity
@Table(name = "produits", uniqueConstraints = { @UniqueConstraint(columnNames = { "label" }) })
public class DbProduit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int produitId;

	@Column(unique = true, nullable = false)
	private String label;
	
	public DbProduit() {
		super();
	}

	public DbProduit(String label) {
		super();
		this.label = label;
	}

	public int getProduitId() {
		return produitId;
	}

	public String getLabel() {
		return label;
	}
}

package com.okayo.facturation.core.model.db;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "produits_souscrits")
public class DbProduitSouscrit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
    @JoinColumn(name = "produits_id", referencedColumnName = "produitId", nullable = false)
    private DbProduit produit;

	@ManyToOne
    @JoinColumn(name = "clients_code", referencedColumnName = "code", nullable = false)
	private DbClient client;
	
	@Column(nullable = false)
	private double quantite;

	@Column(nullable = false)
	private Date date;
	
	public DbProduitSouscrit() {
		super();
	}
	
	public DbProduitSouscrit(DbProduit produit, DbClient client, double quantite,  Date date) {
		super();
		this.produit = produit;
		this.client = client;
		this.quantite = quantite;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}
	
	public DbProduit getProduit() {
		return produit;
	}
	
	public DbClient getClient() {
		return client;
	}
	
	public Date getDate() {
		return date;
	}
	
	public double getQuantite() {
		return quantite;
	}
	
}

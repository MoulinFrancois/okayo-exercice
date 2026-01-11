package com.okayo.facturation.core.model.db;
import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "produit_data")
public class DbProduitData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
    @JoinColumn(name = "produits_id", referencedColumnName = "produitId", nullable = false)
	private DbProduit produit;

	@Column(nullable = false)
	private String designation;
	
	@Column(nullable = false)
	private double tva;
	
	@Column(nullable = false)
	private double prixUnitaireHT;

	@Column(nullable = false)
	private Date dateStart;
	
	@Column(updatable = true)
	private Date dateEnd;
	
	public DbProduitData() {
		super();
	}

	public DbProduitData(DbProduit produit, String designation, double tva, double prixUnitaireHT, Date dateStart) {
		super();
		this.produit = produit;
		this.designation = designation;
		this.tva = tva;
		this.prixUnitaireHT = prixUnitaireHT;
		this.dateStart = dateStart;
	}
	
	
	public DbProduitData(DbProduit produit, String designation, double tva, double prixUnitaireHT,
			Date dateStart, Date dateEnd) {
		super();
		this.produit = produit;
		this.designation = designation;
		this.tva = tva;
		this.prixUnitaireHT = prixUnitaireHT;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	public int getId() {
		return id;
	}
	
	public DbProduit getProduit() {
		return produit;
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

	public Date getDateStart() {
		return dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}		
	
}

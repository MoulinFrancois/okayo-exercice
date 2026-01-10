package com.okayo.facturation.core.model.db;
import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class DbClient {
	
	@Id
	@Column(nullable = false)
	private String code;
	
	@Column(nullable = false)
	private String nom;
	
	@Column(nullable = false)
	private String ligne;
	
	@Column(nullable = false)
	private String codePostal;
	
	@Column(nullable = false)
	private String ville;
	
	public DbClient() {
		super();
	}

	public DbClient(String code, String nom, String ligne, String codePostal, String ville) {
		this.code = code;
		this.nom = nom;
		this.ligne = ligne;
		this.codePostal = codePostal;
		this.ville = ville;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getNom() {
		return nom;
	}

	public String getLigne() {
		return ligne;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setLigne(String ligne) {
		this.ligne = ligne;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}
	
}

package com.okayo.facturation.core.model.db;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "factures")
public class DbFacture {
	
	@Id
	@GenericGenerator(
        name = "year_id_gen", 
        type = YearPrefixGenerator.class
    )
    @GeneratedValue(generator = "year_id_gen")
	private String reference;

	@ManyToOne
    @JoinColumn(name = "clients_code", referencedColumnName = "code", nullable = false)
	private DbClient client;
	
	@Column(nullable = false)
	private Date dateFacturation;
	
	@Column(nullable = false)
	private Date dateEcheance;
	
	public DbFacture() {
		super();
	}

	public DbFacture(String reference, DbClient client, Date dateFacturation, Date dateEcheance) {
		super();
		this.reference = reference;
		this.client = client;
		this.dateFacturation = dateFacturation;
		this.dateEcheance = dateEcheance;
	}

	public DbFacture(DbClient client, Date dateFacturation, Date dateEcheance) {
		super();
		this.client = client;
		this.dateFacturation = dateFacturation;
		this.dateEcheance = dateEcheance;
	}

	public String getReference() {
		return reference;
	}

	public DbClient getClient() {
		return client;
	}

	public Date getDateFacturation() {
		return dateFacturation;
	}

	public Date getDateEcheance() {
		return dateEcheance;
	}

}

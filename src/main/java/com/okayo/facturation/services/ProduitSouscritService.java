package com.okayo.facturation.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitSouscrit;
import com.okayo.facturation.core.model.domain.ProduitSouscrit;
import com.okayo.facturation.core.model.tech.ProduitSouscritRequest;
import com.okayo.facturation.core.utils.db.DbClientRepository;
import com.okayo.facturation.core.utils.db.DbProduitRepository;
import com.okayo.facturation.core.utils.db.DbProduitSouscritRepository;

@Service
public class ProduitSouscritService {

	private DbProduitSouscritRepository produitSouscritRepository;
	private DbProduitRepository produitRepository;
	private DbClientRepository clientRepository;

	@Autowired
	public ProduitSouscritService(DbProduitSouscritRepository produitSouscritRepository,
			DbProduitRepository produitRepository, DbClientRepository clientRepository) {
		this.produitSouscritRepository = produitSouscritRepository;
		this.produitRepository = produitRepository;
		this.clientRepository = clientRepository;
	}

	public void enregistrer(ProduitSouscritRequest ps, Date date) {
		DbProduit produit = produitRepository.findByProduitId(ps.getProduitId())
				.orElseThrow(() -> new IllegalArgumentException("Produit inexistant : " + ps.getProduitId()));
		DbClient client = clientRepository.findByCode(ps.getClientCode())
				.orElseThrow(() -> new IllegalArgumentException("Client inexistant : " + ps.getClientCode()));
		produitSouscritRepository.save(new DbProduitSouscrit(produit, client, ps.getQuantite(), date));
	}

	public List<ProduitSouscrit> tousLesProduitsSouscrits() {
		return produitSouscritRepository.findAll().stream().map(ps -> new ProduitSouscrit(ps.getId(),
				ps.getProduit().getProduitId(), ps.getClient().getCode(), ps.getQuantite(), ps.getDate())).toList();
	}

}
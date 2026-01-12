package com.okayo.facturation.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitSouscrit;
import com.okayo.facturation.core.model.domain.ProduitSouscrit;
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
	
	public void enregistrer(ProduitSouscrit entry, Date date) {
		Optional<DbProduit> produit = produitRepository.findByProduitId(entry.getProduitId());
		if(!produit.isPresent()) {
			throw new IllegalArgumentException("Produit inexistant : " + entry.getProduitId());
		}
		DbClient client = clientRepository.findByCode(entry.getClientCode());
		if(client == null) {
			throw new IllegalArgumentException("Client inexistant : " + entry.getClientCode());
		}
        produitSouscritRepository.save(new DbProduitSouscrit(produit.get(), client, entry.getQuantite(), date));
    }
	
	public List<ProduitSouscrit> tousLesProduitsSouscrits() {
		return produitSouscritRepository.findAll().stream().map(
				ps -> new ProduitSouscrit(ps.getId(), ps.getProduit().getProduitId(), ps.getClient().getCode(), ps.getQuantite(), ps.getDate()))
				.toList();
	}
	
}
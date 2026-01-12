package com.okayo.facturation.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitData;
import com.okayo.facturation.core.model.domain.ProduitData;
import com.okayo.facturation.core.model.tech.ProduitDataRequest;
import com.okayo.facturation.core.utils.db.DbProduitDataRepository;
import com.okayo.facturation.core.utils.db.DbProduitRepository;

@Service
public class ProduitDataService {

	private DbProduitRepository produitRepository;
	private DbProduitDataRepository produitDataRepository;

	@Autowired
	public ProduitDataService(DbProduitRepository produitRepository, DbProduitDataRepository produitDataRepository) {
		this.produitRepository = produitRepository;
		this.produitDataRepository = produitDataRepository;
	}

	@Transactional
	public void sauvegarder(ProduitDataRequest entry, Date date) {

		// on récupere le produit dont on veut mettre à jour les données
		DbProduit produit = produitRepository.findByProduitId(entry.getProduitId()).orElseThrow(
				() -> new IllegalArgumentException("Produit introuvable avec l'ID : " + entry.getProduitId()));

		// on met à jour l'entrée précédente pour le produit, lui fixant une date de fin
		produitDataRepository.updateDateEnd(entry.getProduitId(), date);

		// on crée la nouvelle entrée d'historique pour le produit
		DbProduitData dbEntry = new DbProduitData(produit, entry.getDesignation(), entry.getTva(),
				entry.getPrixUnitaireHT(), date);
		produitDataRepository.save(dbEntry);

	}

	public List<ProduitData> chargerEntreesActuelles() {
		return produitDataRepository.findAllWithoutDateEnd().stream()
				.map(dbEntry -> new ProduitData(dbEntry.getId(), dbEntry.getProduit().getProduitId(),
						dbEntry.getDesignation(), dbEntry.getTva(), dbEntry.getPrixUnitaireHT()))
				.toList();
	}

}

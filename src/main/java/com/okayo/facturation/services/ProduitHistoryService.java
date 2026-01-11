package com.okayo.facturation.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitHistory;
import com.okayo.facturation.core.model.domain.ProduitHistory;
import com.okayo.facturation.core.utils.db.DbProduitHistoryRepository;
import com.okayo.facturation.core.utils.db.DbProduitRepository;

@Service
public class ProduitHistoryService {

    private DbProduitRepository produitRepository;
    private DbProduitHistoryRepository produitHistoryRepository;
    
    @Autowired
	public ProduitHistoryService(DbProduitRepository produitRepository, DbProduitHistoryRepository produitHistoryRepository) {
		this.produitRepository = produitRepository;
		this.produitHistoryRepository = produitHistoryRepository;
	}
    
    @Transactional
	public void sauvegarder(ProduitHistory entry, Date date) {
    	
    	// on récupere le produit dont on veut mettre à jour l'historique
		DbProduit produit = produitRepository.findByProduitId(entry.getProduitId())
				.orElseThrow(() -> new IllegalArgumentException(
						"Produit introuvable avec l'ID : " + entry.getProduitId()));
		
		// on met à jour l'entrée précédente pour le produit, lui fixant une date de fin
		produitHistoryRepository.updateProduitHistory(entry.getProduitId(), date);
		
		// on crée la nouvelle entrée d'historique pour le produit
		DbProduitHistory dbEntry = new DbProduitHistory(produit, entry.getDesignation(), entry.getTva(), entry.getPrixUnitaireHT(), date);
		produitHistoryRepository.save(dbEntry);
		
	}

    public List<ProduitHistory> chargerEntreesActuelles() {
		return produitHistoryRepository.findAllWithoutDateEnd().stream()
				.map(dbEntry -> new ProduitHistory(dbEntry.getProduit().getProduitId(), dbEntry.getDesignation(),
						dbEntry.getTva(), dbEntry.getPrixUnitaireHT()))
				.toList();
    }
	
}

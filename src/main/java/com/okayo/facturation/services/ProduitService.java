package com.okayo.facturation.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.domain.Produit;
import com.okayo.facturation.core.utils.db.DbProduitRepository;

@Service
public class ProduitService {

    @Autowired
    private DbProduitRepository produitRepository;
    
    public void sauvegarder(String labelProduit) {
    	Optional<DbProduit> found = produitRepository.findDbProduitByLabel(labelProduit);
		if (found.isPresent()) {
    		throw new IllegalArgumentException("Un produit avec ce label existe déjà : " + labelProduit);
    	} else {
    		produitRepository.save(new DbProduit(labelProduit));
    	}
    }
    
	public List<Produit> trouverTousLesProduits() {
		return produitRepository.findAll().stream().map(p -> new Produit(p.getProduitId(), p.getLabel())).toList();
	}

}

package com.okayo.facturation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okayo.facturation.core.model.domain.Produit;
import com.okayo.facturation.core.model.tech.ProduitRequest;
import com.okayo.facturation.services.ProduitService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/api/produit")
public class ProduitController {

	@Autowired
	ProduitService produitService;

	@PostMapping(path = "/save", consumes = "application/json", produces = "application/json")
	public void enregistrerProduit(@RequestBody ProduitRequest produitRequest) {
		if (StringUtils.isBlank(produitRequest.getLabel())) {
			throw new IllegalArgumentException("Le nom du produit ne peut pas être vide.");
		}
		produitService.sauvegarder(produitRequest.getLabel());
	}

	@GetMapping(path = "/load-all", produces = "application/json")
	public List<Produit> chargerTousLesProduits() {
		return produitService.trouverTousLesProduits();
	}

}
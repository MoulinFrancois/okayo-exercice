package com.okayo.facturation.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okayo.facturation.core.model.domain.ProduitHistory;
import com.okayo.facturation.services.ProduitHistoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produit-history")
public class ProduitHistoryController {

	@Autowired
	ProduitHistoryService produitHistoryService;

	@PostMapping(path = "/save", consumes = "application/json", produces = "application/json")
	public void enregistrerProduit(@Valid @RequestBody ProduitHistory data) {
		produitHistoryService.sauvegarder(data, new Date());
	}

	@GetMapping(path = "/load-current", produces = "application/json")
	public List<ProduitHistory> chargerTousLesProduits() {
		return produitHistoryService.chargerEntreesActuelles();
	}

}
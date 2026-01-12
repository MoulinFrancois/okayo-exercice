package com.okayo.facturation.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okayo.facturation.core.model.domain.ProduitData;
import com.okayo.facturation.core.model.tech.ProduitDataRequest;
import com.okayo.facturation.services.ProduitDataService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produit-data")
public class ProduitDataController {

	@Autowired
	ProduitDataService produitDataService;

	@PostMapping(path = "/save", consumes = "application/json", produces = "application/json")
	public void enregistrerProduitData(@Valid @RequestBody ProduitDataRequest data) {
		produitDataService.sauvegarder(data, new Date());
	}

	@GetMapping(path = "/load-current", produces = "application/json")
	public List<ProduitData> chargerToutesLesData() {
		return produitDataService.chargerEntreesActuelles();
	}

}
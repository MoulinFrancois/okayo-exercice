package com.okayo.facturation.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okayo.facturation.core.model.domain.ProduitSouscrit;
import com.okayo.facturation.core.model.tech.ProduitSouscritRequest;
import com.okayo.facturation.services.ProduitSouscritService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produit-souscrit")
public class ProduitSouscritController {

	@Autowired
	ProduitSouscritService produitSouscritService;

	@PostMapping(path = "/save", consumes = "application/json", produces = "application/json")
	public void enregistrerProduitData(@Valid @RequestBody ProduitSouscritRequest ps) {
		produitSouscritService.enregistrer(ps, new Date());
	}

	@GetMapping(path = "/load-all", produces = "application/json")
	public List<ProduitSouscrit> chargerToutesLesData() {
		return produitSouscritService.tousLesProduitsSouscrits();
	}

}
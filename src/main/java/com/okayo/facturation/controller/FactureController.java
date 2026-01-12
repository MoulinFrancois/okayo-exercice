package com.okayo.facturation.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okayo.facturation.core.model.domain.Facture;
import com.okayo.facturation.core.model.domain.FactureRequest;
import com.okayo.facturation.services.FactureService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/facture")
public class FactureController {

	@Autowired
	FactureService factureService;

	@PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
	public Facture enregistrerProduit(@Valid @RequestBody FactureRequest facture) {
		return factureService.creerFacturePourClient(facture, new Date());
	}
	
	@GetMapping(path = "/load-all", produces = "application/json")
	public List<String> getFacturesList() {
		return factureService.retrouverToutesLesReferencesFactures();
	}
	
    @GetMapping(path = "/load-by-reference/{reference}", produces = "application/json")
	public Facture getFactureByReference(@PathVariable String reference) {
		return factureService.retrouverFactureParReference(reference);
	}
    
}
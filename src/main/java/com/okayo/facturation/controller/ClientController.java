package com.okayo.facturation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okayo.facturation.core.model.domain.Client;
import com.okayo.facturation.services.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/client")
public class ClientController {
	
	@Autowired
	ClientService clientService;
	
    @PostMapping(path = "/save", consumes = "application/json", produces = "application/json")
    public void enregistrerClient(@Valid @RequestBody Client client) {
    	clientService.enregistrerClient(client);
    }

    @GetMapping(path= "/load-all", produces = "application/json")
    public List<Client> chargerTousLesClients() {
    	return clientService.chargerTousLesClients();
    }
    
}
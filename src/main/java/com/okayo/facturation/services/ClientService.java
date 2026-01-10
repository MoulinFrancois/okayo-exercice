package com.okayo.facturation.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.domain.Client;
import com.okayo.facturation.core.utils.db.DbClientRepository;
import com.okayo.facturation.services.mappers.ClientMapper;

@Service
public class ClientService {

    private DbClientRepository clientRepository;
    
    @Autowired
	public ClientService(DbClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
    
    public void enregistrerClient(Client client) {
    	if (clientRepository.findByCode(client.getCode()) != null) {
    		throw new IllegalArgumentException("CodeClient déjà utilisé : "+client.getCode());
    	}
    	clientRepository.save(ClientMapper.INSTANCE.toDbClient(client));
    }

    public List<Client> chargerTousLesClients() {
    	return clientRepository.findAll().stream().map(this::mapClient).collect(Collectors.toList());
    }

	private Client mapClient(DbClient client) {
		return ClientMapper.INSTANCE.toClient(client);
	}

}

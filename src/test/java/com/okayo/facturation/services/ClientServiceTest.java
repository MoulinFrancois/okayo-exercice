package com.okayo.facturation.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.domain.Adresse;
import com.okayo.facturation.core.model.domain.Client;
import com.okayo.facturation.core.utils.db.DbClientRepository;

@SpringBootTest
public class ClientServiceTest {

	@Autowired
	private ClientService clientService;

	@MockBean
	private DbClientRepository clientRepository;

	@Test
	public void enregistrerClient_casNormal() {
		when(clientRepository.findByCode(anyString())).thenReturn(null);
		clientService.enregistrerClient(new Client("code", "nom", new Adresse("ligne", "75001", "Paris")));
		verify(clientRepository, times(1)).save(any());
	}

	@Test
	public void enregistrerClient_casClientExistant() {
		when(clientRepository.findByCode(anyString())).thenReturn(new DbClient());
		assertThrows(IllegalArgumentException.class, () -> clientService
				.enregistrerClient(new Client("code", "nom", new Adresse("ligne", "75001", "Paris"))));
	}

	@Test
	public void chargerTousLesClients() {
		when(clientRepository.findAll()).thenReturn(new ArrayList<DbClient>() {
			{
				add(new DbClient("code1", "nom1", "ligne", "75012", "PARIS"));
				add(new DbClient("code2", "nom2", "ligne", "75016", "PARIS"));
				add(new DbClient("code3", "nom3", "ligne", "69001", "LYON"));
			}
		});
		// when
		List<Client> clients = clientService.chargerTousLesClients();
		// then
		assertThat(clients).hasSize(3);
		assertThat(clients.stream().map(Client::getCode).toList()).containsExactly("code1", "code2", "code3");
	}

}

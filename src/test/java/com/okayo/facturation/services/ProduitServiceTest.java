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
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.domain.Produit;
import com.okayo.facturation.core.utils.db.DbProduitRepository;

@SpringBootTest
public class ProduitServiceTest {

	@Autowired
	private ProduitService produitService;

	@MockBean
	private DbProduitRepository produitRepository;

	@Test
	public void enregistrerClient_casNormal() {
		when(produitRepository.findDbProduitByLabel(anyString())).thenReturn(Optional.empty());
		produitService.sauvegarder("Nouveau Produit");
		verify(produitRepository, times(1)).save(any());
	}

	@Test
	public void enregistrerClient_casClientExistant() {
		when(produitRepository.findDbProduitByLabel(anyString())).thenReturn(Optional.of(new DbProduit(1, "Nouveau Produit")));
		assertThrows(IllegalArgumentException.class, () -> produitService.sauvegarder("Nouveau Produit"));
	}

	@Test
	public void chargerTousLesClients() {
		when(produitRepository.findAll()).thenReturn(new ArrayList<DbProduit>() {
			{
				add(new DbProduit(1, "Produit A"));
				add(new DbProduit(2, "Produit D"));
				add(new DbProduit(3, "Produit E"));
			}
		});
		// when
		List<Produit> clients = produitService.trouverTousLesProduits();
		// then
		assertThat(clients).hasSize(3);
		assertThat(clients.stream().map(Produit::getId).toList()).containsExactly(1,2,3);
	}

}

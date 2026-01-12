package com.okayo.facturation.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitData;
import com.okayo.facturation.core.model.domain.ProduitData;
import com.okayo.facturation.core.model.tech.ProduitDataRequest;
import com.okayo.facturation.core.utils.db.DbProduitDataRepository;
import com.okayo.facturation.core.utils.db.DbProduitRepository;

@SpringBootTest
public class ProduitDataServiceTest {

	@Autowired
	private ProduitDataService produitDataService;

	@MockBean
	private DbProduitDataRepository produitDataRepository;
	
	@MockBean
	private DbProduitRepository produitRepository;

	private static final Date NOW = new Date();
	private static final DbProduit PRODUIT_A = new DbProduit(1, "ProduitA");
	
	@Test
	public void enregistrerClient_casNormal() {
		when(produitRepository.findByProduitId(PRODUIT_A.getProduitId())).thenReturn(java.util.Optional.of(PRODUIT_A));
		ProduitDataRequest entry = new ProduitDataRequest(PRODUIT_A.getProduitId(), "Le Produit A", 20d, 4000d);
		
		produitDataService.sauvegarder(entry, NOW);
		
		verify(produitRepository, times(1)).findByProduitId(PRODUIT_A.getProduitId());
		verify(produitDataRepository, times(1)).updateDateEnd(PRODUIT_A.getProduitId(), NOW);
		verify(produitDataRepository, times(1)).save(any());
	}

	@Test
	public void enregistrerClient_casProduitInexistant() {
		when(produitRepository.findByProduitId(PRODUIT_A.getProduitId())).thenReturn(java.util.Optional.empty());
		ProduitDataRequest entry = new ProduitDataRequest(PRODUIT_A.getProduitId(), "Le Produit A", 20d, 4000d);
		
		assertThrows(IllegalArgumentException.class, () -> produitDataService.sauvegarder(entry, NOW));
	}
	
	@Test
	public void chargerEntreesActuelles() {
		when(produitDataRepository.findAllWithoutDateEnd())
				.thenReturn(List.of(new DbProduitData(PRODUIT_A, "Le Produit A", 20d, 4000d, NOW),
						new DbProduitData(new DbProduit(2, "ProduitB"), "Le Produit B", 7.5, 125, NOW)));
		
		var entries = produitDataService.chargerEntreesActuelles();
		
		verify(produitDataRepository, times(1)).findAllWithoutDateEnd();
		assertThat(entries).hasSize(2);
		assertThat(entries.stream().map(ProduitData::getProduitId).toList()).containsExactly(1, 2);
		assertThat(entries.stream().map(ProduitData::getDesignation).toList()).containsExactly("Le Produit A", "Le Produit B");
		assertThat(entries.stream().map(ProduitData::getTva).toList()).containsExactly(20d, 7.5);
		assertThat(entries.stream().map(ProduitData::getPrixUnitaireHT).toList()).containsExactly(4000d, 125d);
	}
	
}

package com.okayo.facturation.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitSouscrit;
import com.okayo.facturation.core.model.domain.ProduitSouscrit;
import com.okayo.facturation.core.model.tech.ProduitSouscritRequest;
import com.okayo.facturation.core.utils.db.DbClientRepository;
import com.okayo.facturation.core.utils.db.DbProduitRepository;
import com.okayo.facturation.core.utils.db.DbProduitSouscritRepository;

@SpringBootTest
public class ProduitSouscritServiceTest {

	@Autowired
	private ProduitSouscritService produitSouscritService;

	@MockBean
	private DbProduitSouscritRepository produitSouscritRepository;

	@MockBean
	private DbProduitRepository produitRepository;

	@MockBean
	private DbClientRepository clientRepository;

	private static final Date NOW = new Date();
	private static final DbProduit PRODUIT_A = new DbProduit(1, "ProduitA");
	private static final DbClient CLIENT_1 = new DbClient("client1", "nom", "ligne", "75014", "PARIS");

	@Test
	public void enregistrerClient_casNormal() {
		when(produitRepository.findByProduitId(PRODUIT_A.getProduitId())).thenReturn(java.util.Optional.of(PRODUIT_A));
		when(clientRepository.findByCode(CLIENT_1.getCode())).thenReturn(Optional.of(CLIENT_1));

		ProduitSouscritRequest entry = new ProduitSouscritRequest(PRODUIT_A.getProduitId(), CLIENT_1.getCode(), 10d);
		produitSouscritService.enregistrer(entry, NOW);

		verify(produitRepository, times(1)).findByProduitId(PRODUIT_A.getProduitId());
		verify(clientRepository, times(1)).findByCode(CLIENT_1.getCode());
		verify(produitSouscritRepository, times(1)).save(any());
	}

	@Test
	public void enregistrerClient_casProduitInexistant() {
		when(produitRepository.findByProduitId(PRODUIT_A.getProduitId())).thenReturn(java.util.Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			ProduitSouscritRequest entry = new ProduitSouscritRequest(PRODUIT_A.getProduitId(), CLIENT_1.getCode(), 10d);
			produitSouscritService.enregistrer(entry, NOW);
		});
	}

	@Test
	public void enregistrerClient_casClientInexistant() {
		when(produitRepository.findByProduitId(PRODUIT_A.getProduitId())).thenReturn(java.util.Optional.of(PRODUIT_A));
		when(clientRepository.findByCode(CLIENT_1.getCode())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			ProduitSouscritRequest entry = new ProduitSouscritRequest(PRODUIT_A.getProduitId(), CLIENT_1.getCode(), 10d);
			produitSouscritService.enregistrer(entry, NOW);
		});
	}

	@Test
	public void tousLesProduitsSouscrits() {
		when(produitSouscritRepository.findAll())
				.thenReturn(List.of(new DbProduitSouscrit(PRODUIT_A, CLIENT_1, 10d, NOW)));

		List<ProduitSouscrit> resultat = produitSouscritService.tousLesProduitsSouscrits();

		assertThat(resultat).hasSize(1);
		assertThat(resultat.get(0).getClientCode()).isEqualTo(CLIENT_1.getCode());
		assertThat(resultat.get(0).getProduitId()).isEqualTo(PRODUIT_A.getProduitId());
		assertThat(resultat.get(0).getQuantite()).isEqualTo(10);
		assertThat(resultat.get(0).getDate()).isEqualTo(NOW);
	}

}

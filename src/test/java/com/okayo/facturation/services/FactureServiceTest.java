package com.okayo.facturation.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.db.DbFacture;
import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitData;
import com.okayo.facturation.core.model.db.DbProduitSouscrit;
import com.okayo.facturation.core.model.domain.FacturationElement;
import com.okayo.facturation.core.model.domain.Facture;
import com.okayo.facturation.core.model.domain.FactureRequest;
import com.okayo.facturation.core.utils.db.DbClientRepository;
import com.okayo.facturation.core.utils.db.DbFactureRepository;
import com.okayo.facturation.core.utils.db.DbProduitDataRepository;
import com.okayo.facturation.core.utils.db.DbProduitSouscritRepository;

@SpringBootTest
public class FactureServiceTest {

	@Autowired
	private FactureService factureService;

	@MockBean
	private DbClientRepository clientRepository;
	@MockBean
	private DbProduitDataRepository produitDataRepository;
	@MockBean
	private DbProduitSouscritRepository produitSouscritRepository;
	@MockBean
	private DbFactureRepository factureRepository;

	private static final Date NOW = new Date();
	private static final Date TOMORROW = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
	private static final Date _1h = new Date(System.currentTimeMillis() - 60 * 60 * 1000);
	private static final DbProduit PRODUIT_A = new DbProduit(1, "ProduitA");
	private static final DbProduit PRODUIT_B = new DbProduit(2, "ProduitB");
	private static final DbProduit PRODUIT_C = new DbProduit(3, "ProduitC");
	private static final DbClient CLIENT_1 = new DbClient("client1", "nom", "ligne", "75014", "PARIS");
	private static final DbProduitSouscrit PRODUIT_SOUSCRIT_1 = new DbProduitSouscrit(PRODUIT_A, CLIENT_1, 10d, _1h);
	private static final DbProduitSouscrit PRODUIT_SOUSCRIT_2 = new DbProduitSouscrit(PRODUIT_B, CLIENT_1, 5d, _1h);
	private static final DbProduitSouscrit PRODUIT_SOUSCRIT_3 = new DbProduitSouscrit(PRODUIT_C, CLIENT_1, 2d, _1h);
	private static final DbProduitSouscrit PRODUIT_SOUSCRIT_4 = new DbProduitSouscrit(PRODUIT_B, CLIENT_1, 3d, _1h);
	private static final DbProduitData PRODUIT_DATA_A = new DbProduitData(PRODUIT_A, "Mon produit A", 20d, 100d, _1h);
	private static final DbProduitData PRODUIT_DATA_B = new DbProduitData(PRODUIT_B, "Mon produit B", 7d, 2000d, _1h);
	private static final DbProduitData PRODUIT_DATA_C = new DbProduitData(PRODUIT_C, "Le produit C", 5.2, 4500d, _1h);

	@Test
	public void enregistrerClient_casNormal() {
		when(clientRepository.findByCode(CLIENT_1.getCode())).thenReturn(CLIENT_1);
		when(factureRepository.save(any())).thenReturn(new DbFacture(CLIENT_1, NOW, TOMORROW));
		when(produitSouscritRepository.findByClientAndDate(eq(CLIENT_1.getCode()), any(), eq(NOW)))
				.thenReturn(List.of(PRODUIT_SOUSCRIT_1, PRODUIT_SOUSCRIT_2, PRODUIT_SOUSCRIT_3, PRODUIT_SOUSCRIT_4));
		when(produitDataRepository.findByProduitAndDate(PRODUIT_A.getProduitId(), NOW)).thenReturn(PRODUIT_DATA_A);
		when(produitDataRepository.findByProduitAndDate(PRODUIT_B.getProduitId(), NOW)).thenReturn(PRODUIT_DATA_B);
		when(produitDataRepository.findByProduitAndDate(PRODUIT_C.getProduitId(), NOW)).thenReturn(PRODUIT_DATA_C);
		
		factureService.creerFacturePourClient(new FactureRequest(CLIENT_1.getCode(), TOMORROW), NOW);
		
		verify(factureRepository, times(1)).save(any());
	}
	
	@Test
	public void enregistrerClient_casClientInexistant() {
		when(clientRepository.findByCode(CLIENT_1.getCode())).thenThrow(new IllegalArgumentException("Client inexistant : " + CLIENT_1.getCode()));
		FactureRequest request = new FactureRequest(CLIENT_1.getCode(), TOMORROW);
		
		assertThrows(IllegalArgumentException.class, () -> {
			factureService.creerFacturePourClient(request, NOW);
		});
		verify(factureRepository, times(0)).save(any());
	}
	
	@Test
	public void enregistrerClient_casRienAFacturer() {
		when(clientRepository.findByCode(CLIENT_1.getCode())).thenReturn(CLIENT_1);
		when(produitSouscritRepository.findByClientAndDate(eq(CLIENT_1.getCode()), any(), eq(NOW)))
				.thenReturn(List.of());
		FactureRequest request = new FactureRequest(CLIENT_1.getCode(), TOMORROW);
		
		assertThrows(IllegalArgumentException.class, () -> {
			factureService.creerFacturePourClient(request, NOW);
		});
		verify(factureRepository, times(0)).save(any());
	}
	
	@Test
	public void retrouverToutesLesReferencesFactures() {
		when(factureRepository.findAll()).thenReturn(List.of(
				new DbFacture("2026-001",CLIENT_1, NOW, TOMORROW) {},
				new DbFacture("2026-002",CLIENT_1, NOW, TOMORROW) {}, 
				new DbFacture("2026-003",CLIENT_1, NOW, TOMORROW) {}));

		List<String> references = factureService.retrouverToutesLesReferencesFactures();

		assertThat(references).hasSize(3);
		assertThat(references).containsExactly("2026-001", "2026-002", "2026-003");
	}
	
	@Test
	public void retrouverFactureParReference() {
		//given
		when(factureRepository.findByReference("2026-001"))
				.thenReturn(new DbFacture("2026-001", CLIENT_1, NOW, TOMORROW) {
				});
		when(factureRepository.findLastDateFacturationBeforeReferenceForClient(CLIENT_1.getCode(), NOW)).thenReturn(null);
		when(produitDataRepository.findByProduitAndDate(PRODUIT_A.getProduitId(), NOW)).thenReturn(PRODUIT_DATA_A);
		when(produitDataRepository.findByProduitAndDate(PRODUIT_B.getProduitId(), NOW)).thenReturn(PRODUIT_DATA_B);
		when(produitDataRepository.findByProduitAndDate(PRODUIT_C.getProduitId(), NOW)).thenReturn(PRODUIT_DATA_C);
		when(produitSouscritRepository.findByClientAndDate(eq(CLIENT_1.getCode()), any(), eq(NOW)))
				.thenReturn(List.of(PRODUIT_SOUSCRIT_1, PRODUIT_SOUSCRIT_2, PRODUIT_SOUSCRIT_3, PRODUIT_SOUSCRIT_4));
		
	    //when
		Facture facture = factureService.retrouverFactureParReference("2026-001");

		//then
		assertThat(facture.getReference()).isEqualTo("2026-001");
		assertThat(facture.getClient().getCode()).isEqualTo(CLIENT_1.getCode());
		assertThat(facture.getElements()).hasSize(3);
		
		FacturationElement fragmentA = facture.getElements().stream().filter(e -> e.getDesignation().equals("Mon produit A")).findFirst().get();
		assertThat(fragmentA.getPrixUnitaireHT()).isEqualTo(100d);
		assertThat(fragmentA.getQuantite()).isEqualTo(10d);
		assertThat(fragmentA.getTva()).isEqualTo(20d);
		assertThat(fragmentA.getTotalHT()).isEqualTo(1000d);
		
		FacturationElement fragmentB = facture.getElements().stream().filter(e -> e.getDesignation().equals("Mon produit B")).findFirst().get();
		assertThat(fragmentB.getPrixUnitaireHT()).isEqualTo(2000d);
		assertThat(fragmentB.getQuantite()).isEqualTo(8d);
		assertThat(fragmentB.getTva()).isEqualTo(7d);
		assertThat(fragmentB.getTotalHT()).isEqualTo(16000d);
		
		FacturationElement fragmentC = facture.getElements().stream().filter(e -> e.getDesignation().equals("Le produit C")).findFirst().get();
		assertThat(fragmentC.getPrixUnitaireHT()).isEqualTo(4500d);
		assertThat(fragmentC.getQuantite()).isEqualTo(2d);
		assertThat(fragmentC.getTva()).isEqualTo(5.2d);
		assertThat(fragmentC.getTotalHT()).isEqualTo(9000d);
		
		assertThat(facture.getDateFacturation()).isEqualTo(NOW);
		assertThat(facture.getDateEcheance()).isEqualTo(TOMORROW);
		
		assertThat(facture.getTotalTVAParTaux()).hasSize(3);
		assertThat(facture.getTotalTVAParTaux().get(20d)).isEqualTo(200d, offset(0.01d));
		assertThat(facture.getTotalTVAParTaux().get(7d)).isEqualTo(1120d, offset(0.01d));
		assertThat(facture.getTotalTVAParTaux().get(5.2d)).isEqualTo(468d, offset(0.01d));
		
		assertThat(facture.getTotalHT()).isEqualTo(26000d);
		assertThat(facture.getTotalTTC()).isEqualTo(27788d);
	}
	
}

package com.okayo.facturation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.okayo.facturation.core.model.domain.Adresse;
import com.okayo.facturation.core.model.domain.Client;
import com.okayo.facturation.core.model.domain.FacturationElement;
import com.okayo.facturation.core.model.domain.Facture;
import com.okayo.facturation.services.FactureService;

@WebMvcTest(FactureController.class)
@AutoConfigureMockMvc
public class FactureControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FactureService factureService;

	private static final Date _12janv = new Date(1768176000L * 1000);
	private static final Date _17janv = new Date(1768176000L * 1000 + 5 * 24 * 3600 * 1000);
	private static final List<FacturationElement> facturationElements = List.of(
			new FacturationElement("Le produit A", 20.0, 58000.0, 4.0, 232000.0),
			new FacturationElement("Mon produit B", 7.0, 4000.0, 5.0, 20000.0),
			new FacturationElement("Produit C", 5.0, 200.0, 14.0, 2800.0),
			new FacturationElement("Le Produit D", 20.0, 157.90, 60.0, 9474.0));
	private static final Facture factureCreee = new Facture("2026-001",
			new Client("Client 1", "Martine Brun", new Adresse("1, rue du test", "75012", "Paris")),
			facturationElements, _12janv, _17janv);

	@Test
	void creerFacturePourClient_ok() throws Exception {
		when(factureService.creerFacturePourClient(any(), any())).thenReturn(factureCreee);
		String expectedJsonContent = new String(
				FactureControllerTest.class.getResourceAsStream("./expectedFacture.json").readAllBytes(),
				StandardCharsets.UTF_8);

		mockMvc.perform(post("/api/facture/create")
				.content("{\"clientCode\":\"Client1\",\"dateEcheance\":\"2026-01-17T00:00:00.000+00:00\"}")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedJsonContent));
	}

	@Test
	void creerFacturePourClient_clientInexistant() throws Exception {
		when(factureService.creerFacturePourClient(any(), any()))
				.thenThrow(new IllegalArgumentException("Client introuvable avec le code : Client1"));

		mockMvc.perform(post("/api/facture/create")
				.content("{\"clientCode\":\"Client1\",\"dateEcheance\":\"2026-01-17T00:00:00.000+00:00\"}")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Client introuvable avec le code : Client1"));
	}

	@Test
	void creerFacturePourClient_rienAFacturer() throws Exception {
		when(factureService.creerFacturePourClient(any(), any()))
				.thenThrow(new IllegalArgumentException("Aucun produit à facturer pour le client : Client1"));

		mockMvc.perform(post("/api/facture/create")
				.content("{\"clientCode\":\"Client1\",\"dateEcheance\":\"2026-01-17T00:00:00.000+00:00\"}")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Aucun produit à facturer pour le client : Client1"));
	}

	@Test
	void retrouverToutesLesReferencesFactures() throws Exception {
		when(factureService.retrouverToutesLesReferencesFactures())
				.thenReturn(List.of("2026-001", "2026-002", "2026-003"));

		mockMvc.perform(get("/api/facture/load-all")).andExpect(status().isOk())
				.andExpect(content().json("[\"2026-001\",\"2026-002\",\"2026-003\"]"));
	}

	@Test
	void retrouverFactureParReference_ok() throws Exception {
		when(factureService.retrouverFactureParReference("2026-001")).thenReturn(factureCreee);
		String expectedJsonContent = new String(
				FactureControllerTest.class.getResourceAsStream("./expectedFacture.json").readAllBytes(),
				StandardCharsets.UTF_8);

		mockMvc.perform(get("/api/facture/load-by-reference/2026-001").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedJsonContent));
	}

	@Test
	void retrouverFactureParReference_referenceInconnue() throws Exception {
		when(factureService.retrouverFactureParReference("2020-001"))
				.thenThrow(new IllegalArgumentException("Facture introuvable avec la référence : 2020-001"));

		mockMvc.perform(get("/api/facture/load-by-reference/2020-001").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Facture introuvable avec la référence : 2020-001"));
	}

}

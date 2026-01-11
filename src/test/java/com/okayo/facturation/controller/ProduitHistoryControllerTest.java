package com.okayo.facturation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.okayo.facturation.core.model.domain.ProduitHistory;
import com.okayo.facturation.services.ProduitHistoryService;

@WebMvcTest(ProduitHistoryController.class)
@AutoConfigureMockMvc
public class ProduitHistoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProduitHistoryService produitHistoryService;

	@Test
	void loadActuelles() throws Exception {
		when(produitHistoryService.chargerEntreesActuelles()).thenReturn(List
				.of(new ProduitHistory(1, "Produit A", 20d, 4000d), new ProduitHistory(2, "Produit B", 15d, 3000d)));
		
		String expected = """
				[{"id":null,"produitId":1,"designation":"Produit A","tva":20.0,"prixUnitaireHT":4000.0},
				 {"id":null,"produitId":2,"designation":"Produit B","tva":15.0,"prixUnitaireHT":3000.0}]
				""";
		mockMvc.perform(get("/api/produit-history/load-current"))
		.andExpect(status().isOk())
		.andExpect(content().json(expected));
	}

	@Test
	void enregistrerProduitHistory_ok() throws Exception {
		doNothing().when(produitHistoryService).sauvegarder(any(), any());
		mockMvc.perform(post("/api/produit-history/save")
				.content("{\"produitId\":1,\"designation\":\"Produit A\",\"tva\":20.0,\"prixUnitaireHT\":4000.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void enregistrerProduitHistory_existant() throws Exception {
		doThrow(new IllegalArgumentException("Produit introuvable avec l'ID : 42"))
				.when(produitHistoryService).sauvegarder(any(), any());
		mockMvc.perform(post("/api/produit-history/save")
				.content("{\"produitId\":1,\"designation\":\"Produit A\",\"tva\":20.0,\"prixUnitaireHT\":4000.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Produit introuvable avec l'ID : 42"));
	}

	@Test
	void enregistrerProduitHistory_contentInvalide() throws Exception {
		doNothing().when(produitHistoryService).sauvegarder(any(), any());
		mockMvc.perform(post("/api/produit-history/save")
				.content("{\"designation\":\"Produit A\",\"tva\":20.0,\"prixUnitaireHT\":4000.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(content().json("{\"errors\":[\"produitId: must not be null\"]}"));
	}
	
}

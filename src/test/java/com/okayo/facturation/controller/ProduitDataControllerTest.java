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

import com.okayo.facturation.core.model.domain.ProduitData;
import com.okayo.facturation.services.ProduitDataService;

@WebMvcTest(ProduitDataController.class)
@AutoConfigureMockMvc
public class ProduitDataControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProduitDataService produitDataService;

	@Test
	void loadActuelles() throws Exception {
		when(produitDataService.chargerEntreesActuelles()).thenReturn(List
				.of(new ProduitData(1, "Produit A", 20d, 4000d), new ProduitData(2, "Produit B", 15d, 3000d)));
		
		String expected = """
				[{"id":null,"produitId":1,"designation":"Produit A","tva":20.0,"prixUnitaireHT":4000.0},
				 {"id":null,"produitId":2,"designation":"Produit B","tva":15.0,"prixUnitaireHT":3000.0}]
				""";
		mockMvc.perform(get("/api/produit-data/load-current"))
		.andExpect(status().isOk())
		.andExpect(content().json(expected));
	}

	@Test
	void enregistrerProduitData_ok() throws Exception {
		doNothing().when(produitDataService).sauvegarder(any(), any());
		mockMvc.perform(post("/api/produit-data/save")
				.content("{\"produitId\":1,\"designation\":\"Produit A\",\"tva\":20.0,\"prixUnitaireHT\":4000.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void enregistrerProduitData_existant() throws Exception {
		doThrow(new IllegalArgumentException("Produit introuvable avec l'ID : 42"))
				.when(produitDataService).sauvegarder(any(), any());
		mockMvc.perform(post("/api/produit-data/save")
				.content("{\"produitId\":1,\"designation\":\"Produit A\",\"tva\":20.0,\"prixUnitaireHT\":4000.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Produit introuvable avec l'ID : 42"));
	}

	@Test
	void enregistrerProduitData_contentInvalide() throws Exception {
		doNothing().when(produitDataService).sauvegarder(any(), any());
		mockMvc.perform(post("/api/produit-data/save")
				.content("{\"designation\":\"Produit A\",\"tva\":20.0,\"prixUnitaireHT\":4000.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(content().json("{\"errors\":[\"produitId: must not be null\"]}"));
	}
	
}

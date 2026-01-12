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

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.okayo.facturation.core.model.domain.ProduitSouscrit;
import com.okayo.facturation.services.ProduitSouscritService;

@WebMvcTest(ProduitSouscritController.class)
@AutoConfigureMockMvc
public class ProduitSouscritControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProduitSouscritService produitSouscritService;
	
	@Test
	void enregistrerProduitData_ok() throws Exception {
		doNothing().when(produitSouscritService).enregistrer(any(), any());
		mockMvc.perform(post("/api/produit-souscrit/save")
				.content("{\"produitId\":1,\"clientCode\":\"Client1\",\"quantite\":20.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void enregistrerProduitData_produitInexistant() throws Exception {
		doThrow(new IllegalArgumentException("Produit introuvable avec l'ID : 1"))
				.when(produitSouscritService).enregistrer(any(), any());
		mockMvc.perform(post("/api/produit-souscrit/save")
				.content("{\"produitId\":1,\"clientCode\":\"Client1\",\"quantite\":20.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Produit introuvable avec l'ID : 1"));
	}

	@Test
	void enregistrerProduitData_existant() throws Exception {
		doThrow(new IllegalArgumentException("Client introuvable avec l'ID : Client1"))
				.when(produitSouscritService).enregistrer(any(), any());
		mockMvc.perform(post("/api/produit-souscrit/save")
				.content("{\"produitId\":1,\"clientCode\":\"Client1\",\"quantite\":20.0}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Client introuvable avec l'ID : Client1"));
	}

	@Test
	void enregistrerProduitData_contentInvalide() throws Exception {
		doNothing().when(produitSouscritService).enregistrer(any(), any());
		mockMvc.perform(post("/api/produit-souscrit/save")
				.content("{\"produitId\":1,\"clientCode\":\"Client1\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(content().json("{\"errors\":[\"quantite: must not be null\"]}"));
	}

	private static final Date _12janv = new Date(1768176000L * 1000);
	
	@Test
	void loadall() throws Exception {
		when(produitSouscritService.tousLesProduitsSouscrits()).thenReturn(
				List.of(new ProduitSouscrit(1,1,"Client1",20.0, _12janv), new ProduitSouscrit(2,2,"Client2",4.0, _12janv)));
		mockMvc.perform(get("/api/produit-souscrit/load-all"))
				.andExpect(status().isOk())
				.andExpect(content().string("[{\"id\":1,\"produitId\":1,\"clientCode\":\"Client1\",\"quantite\":20.0,\"date\":\"2026-01-12T00:00:00.000+00:00\"},"
						+ "{\"id\":2,\"produitId\":2,\"clientCode\":\"Client2\",\"quantite\":4.0,\"date\":\"2026-01-12T00:00:00.000+00:00\"}]"));
	}

}
package com.okayo.facturation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.okayo.facturation.services.ProduitService;

@WebMvcTest(ProduitController.class)
@AutoConfigureMockMvc
public class ProduitControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProduitService produitService;

	@Test
	void loadall() throws Exception {
		when(produitService.trouverTousLesProduits()).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/api/produit/load-all")).andExpect(status().isOk());
	}

	@Test
	void enregistrerProduit_ok() throws Exception {
		doNothing().when(produitService).sauvegarder(anyString());
		mockMvc.perform(post("/api/produit/save").content("{\"label\":\"Produit Test\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void enregistrerProduit_existant() throws Exception {
		doThrow(new IllegalArgumentException("Un produit avec ce label existe déjà : Produit Test"))
				.when(produitService).sauvegarder(any());
		mockMvc.perform(post("/api/produit/save").content("{\"label\":\"Produit Test\"}").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Un produit avec ce label existe déjà : Produit Test"));
	}

	@Test
	void enregistrerProduit_contentInvalide() throws Exception {
		doNothing().when(produitService).sauvegarder(any());
		mockMvc.perform(post("/api/produit/save").content("{ }").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.body.detail").value("Le nom du produit ne peut pas être vide."));
	}
	
}

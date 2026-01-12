package com.okayo.facturation.controller;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.okayo.facturation.core.model.domain.Facture;
import com.okayo.facturation.services.FactureService;
import com.okayo.facturation.services.PdfService;

@Controller
public class PdfController {

	@Autowired
	FactureService factureService;

	@Autowired
	PdfService pdfService;

	@GetMapping("/download/pdf/{reference}")
	public ResponseEntity<byte[]> downloadPdf(Model model, @PathVariable String reference) {

		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML");

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(resolver);

		Facture facture = factureService.retrouverFactureParReference(reference);
		fillModel(model, facture);

		byte[] pdfBytes = pdfService.generatePdfFromModel("facture", model.asMap());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=facture-" + reference + ".pdf")
				.contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}

	@GetMapping("/view/pdf/{reference}")
	public String html(Model model, @PathVariable String reference) {
		Facture facture = factureService.retrouverFactureParReference(reference);
		fillModel(model, facture);
		return "facture";
	}

	private void fillModel(Model model, Facture facture) {
		model.addAttribute("clientcode", facture.getClient().getCode());
		model.addAttribute("reference", facture.getReference());
		model.addAttribute("datefacturation", formatDate(facture.getDateFacturation()));
		model.addAttribute("dateecheance", formatDate(facture.getDateEcheance()));
		model.addAttribute("clientnom", facture.getClient().getNom());
		model.addAttribute("clientligne", facture.getClient().getAdresse().getLigne());
		model.addAttribute("clientville",
				facture.getClient().getAdresse().getCodePostal() + " " + facture.getClient().getAdresse().getVille());
		model.addAttribute("factureelements", facture.getElements());
		model.addAttribute("totalht", facture.getTotalHT());
		model.addAttribute("tva", facture.getTotalTVAParTaux());
		model.addAttribute("totalttc", facture.getTotalTTC());
	}

	private String formatDate(Date date) {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy")
				.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}

}
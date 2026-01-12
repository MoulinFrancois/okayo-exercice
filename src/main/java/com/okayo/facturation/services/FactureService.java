package com.okayo.facturation.services;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toMap;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.db.DbFacture;
import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitData;
import com.okayo.facturation.core.model.db.DbProduitSouscrit;
import com.okayo.facturation.core.model.domain.FacturationElement;
import com.okayo.facturation.core.model.domain.Facture;
import com.okayo.facturation.core.model.tech.FactureRequest;
import com.okayo.facturation.core.utils.db.DbClientRepository;
import com.okayo.facturation.core.utils.db.DbFactureRepository;
import com.okayo.facturation.core.utils.db.DbProduitDataRepository;
import com.okayo.facturation.core.utils.db.DbProduitSouscritRepository;
import com.okayo.facturation.services.mappers.ClientMapper;

@Service
public class FactureService {

	private DbClientRepository clientRepository;
	private DbProduitDataRepository produitDataRepository;
	private DbProduitSouscritRepository produitSouscritRepository;
	private DbFactureRepository factureRepository;

	@Autowired
	public FactureService(DbClientRepository clientRepository, DbProduitDataRepository produitDataRepository,
			DbProduitSouscritRepository produitSouscritRepository, DbFactureRepository factureRepository) {
		this.clientRepository = clientRepository;
		this.produitDataRepository = produitDataRepository;
		this.produitSouscritRepository = produitSouscritRepository;
		this.factureRepository = factureRepository;
	}

	public List<String> retrouverToutesLesReferencesFactures() {
		return factureRepository.findAll().stream().map(DbFacture::getReference).toList();
	}

	public Facture creerFacturePourClient(FactureRequest fr, Date date) {
		DbClient client = clientRepository.findByCode(fr.getClientCode())
				.orElseThrow(() -> new IllegalArgumentException("Client inexistant : " + fr.getClientCode()));
		List<DbProduitSouscrit> produitsAFacturer = findProduitsAFacturer(client.getCode(), date);
		if (produitsAFacturer.isEmpty()) {
			throw new IllegalArgumentException("Aucun produit à facturer pour le client : " + fr.getClientCode());
		}
		DbFacture factureCreee = factureRepository.save(new DbFacture(client, new Date(), fr.getDateEcheance()));
		return buildFacture(factureCreee);
	}

	public Facture retrouverFactureParReference(String reference) {
		DbFacture dbFacture = factureRepository.findByReference(reference);
		if (dbFacture == null) {
			throw new IllegalArgumentException("Facture inexistante : " + reference);
		}
		return buildFacture(dbFacture);
	}

	private Facture buildFacture(DbFacture dbFacture) {
		return new Facture(dbFacture.getReference(), ClientMapper.INSTANCE.toClient(dbFacture.getClient()),
				buildFacturationElements(dbFacture), dbFacture.getDateFacturation(), dbFacture.getDateEcheance());
	}

	private List<FacturationElement> buildFacturationElements(DbFacture dbFacture) {

		// On trouve les produits à facturer
		List<DbProduitSouscrit> produitsAFacturer = findProduitsAFacturer(dbFacture.getClient().getCode(),
				dbFacture.getDateFacturation());

		// on regroupe par produit en sommant les quantités (si plusieurs commandes d'un
		// produit on veut une seule ligne dans la facture)
		Map<DbProduit, Double> quantitesRegroupees = produitsAFacturer.stream()
				.collect(groupingBy(DbProduitSouscrit::getProduit, summingDouble(DbProduitSouscrit::getQuantite)));

		// on recupere les élements de pricing pour la date de la facture
		Map<DbProduit, DbProduitData> produitsData = quantitesRegroupees.keySet().stream()
				.collect(toMap(dbProduit -> dbProduit,
						dbProduit -> getProduitData(dbProduit.getProduitId(), dbFacture.getDateFacturation())));

		// on calcule les élements de facturation
		return quantitesRegroupees.keySet().stream().map(dbProduit -> {
			DbProduitData data = produitsData.get(dbProduit);
			double quantite = quantitesRegroupees.get(dbProduit).doubleValue();
			return new FacturationElement(data.getDesignation(), data.getTva(), data.getPrixUnitaireHT(), quantite,
					data.getPrixUnitaireHT() * quantite);
		}).toList();
	}

	private List<DbProduitSouscrit> findProduitsAFacturer(String clientCode, Date dateFacturation) {
		Date dateDerniereFacturation = factureRepository.findLastDateFacturationBeforeReferenceForClient(clientCode,
				dateFacturation);

		// on recupere les produits souscrits entre la derniere facturation et la date
		// de facturation de la facture qu'on traite
		List<DbProduitSouscrit> produitsAFacturer = produitSouscritRepository.findByClientAndDate(clientCode,
				dateDerniereFacturation != null ? dateDerniereFacturation : new Date(0), dateFacturation);
		return produitsAFacturer;
	}

	private DbProduitData getProduitData(int produitId, Date date) {
		return produitDataRepository.findByProduitAndDate(produitId, date);
	}

}

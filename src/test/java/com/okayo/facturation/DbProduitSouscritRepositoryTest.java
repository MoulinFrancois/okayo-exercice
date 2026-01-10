package com.okayo.facturation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitSouscrit;
import com.okayo.facturation.core.utils.db.DbProduitSouscritRepository;

@DataJpaTest
public class DbProduitSouscritRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private DbProduitSouscritRepository dbProduitSouscritRepository;

	@Test
	public void insertion() {
		// Given
		DbProduit produit = entityManager.persist(new DbProduit("ProduitTest"));
		DbClient client = entityManager.persist(new DbClient("CLIENT1", "Michel","1 rue de la paix", "75000", "Paris"));
		DbProduitSouscrit souscrit = new DbProduitSouscrit(produit, client, 4, new Date());
		
		// When
        DbProduitSouscrit saved = dbProduitSouscritRepository.save(souscrit);
        
        // Then
        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getProduit()).isEqualTo(souscrit.getProduit());
        assertThat(saved.getClient()).isEqualTo(souscrit.getClient());
        assertThat(saved.getQuantite()).isEqualTo(souscrit.getQuantite());
        assertThat(saved.getDate()).isEqualTo(souscrit.getDate());
	}
	
	@Test
	public void trouverParClientEtDate() {
    	Date now = new Date();
    	Date _4h = Date.from(new Date().toInstant().minusSeconds(4*3600));
    	Date _3h = Date.from(new Date().toInstant().minusSeconds(3*3600));
    	Date _2h = Date.from(new Date().toInstant().minusSeconds(2*3600));
    	Date _1h = Date.from(new Date().toInstant().minusSeconds(3600));
		DbProduit produit = entityManager.persist(new DbProduit("ProduitTest"));
		DbProduit produit2 = entityManager.persist(new DbProduit("ProduitTest2"));
		DbClient client = entityManager.persist(new DbClient("CLIENT1", "Michel","1 rue de la paix", "75001", "Paris"));
		DbClient client2 = entityManager.persist(new DbClient("CLIENT2", "Diego","13 rue Daguerre", "75014", "Paris"));
		DbProduitSouscrit souscrit1 = entityManager.persist(new DbProduitSouscrit(produit, client, 4, _3h));
		DbProduitSouscrit souscrit2 = entityManager.persist(new DbProduitSouscrit(produit, client2, 3, _3h));
		DbProduitSouscrit souscrit3 = entityManager.persist(new DbProduitSouscrit(produit2, client, 3, _1h));

		List<DbProduitSouscrit> results = dbProduitSouscritRepository.findByClientAndDate("CLIENT1", _4h, now);
        assertThat(results.size()).isEqualTo(2);
        assertThat(results).containsExactly(souscrit1, souscrit3);
        
		List<DbProduitSouscrit> results2 = dbProduitSouscritRepository.findByClientAndDate("CLIENT2", _4h, now);
        assertThat(results2.size()).isEqualTo(1);
        assertThat(results2).containsExactly(souscrit2);
        
		List<DbProduitSouscrit> results3 = dbProduitSouscritRepository.findByClientAndDate("CLIENT1", _2h, now);
        assertThat(results3.size()).isEqualTo(1);
        assertThat(results3).containsExactly( souscrit3);
	}
	
}

package com.okayo.facturation.core.utils.db;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.db.DbProduitData;

@DataJpaTest
public class DbProduitDataRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DbProduitRepository dbProduitRepository;

    @Autowired
    private DbProduitDataRepository dbProduitDataRepository;

    @Test
    public void insertion() {
        // Given
    	Date now = new Date();
    	DbProduit produitA = entityManager.persist(new DbProduit("Produit A"));
        DbProduitData history = new DbProduitData(produitA, "Mon Produit A", 20, 4000, now);

        // When
        DbProduitData inserted =  dbProduitDataRepository.save(history);

        // Then
        assertThat(inserted.getId()).isGreaterThan(0);
        assertThat(inserted.getProduit()).isEqualTo(history.getProduit());
        assertThat(inserted.getDesignation()).isEqualTo(history.getDesignation());
        assertThat(inserted.getPrixUnitaireHT()).isEqualTo(history.getPrixUnitaireHT());
        assertThat(inserted.getDateStart()).isEqualTo(history.getDateStart());
        assertThat(inserted.getDateEnd()).isNull();
    }

    @Test
    public void updateProduitHistory() {
        // Given
    	Date now = new Date();
    	Date avant = Date.from(new Date().toInstant().minusSeconds(3600));
    	DbProduit produitA = entityManager.persist(new DbProduit("Produit A"));
        DbProduitData inserted =  entityManager.persist(new DbProduitData(produitA, "Mon Produit A", 20, 4000, avant));

        // When
        int modified = dbProduitDataRepository.updateDateEnd(produitA.getProduitId(), now);
        entityManager.clear();
        
        // Then
        assertThat(modified).isEqualTo(1);
        DbProduitData updated = entityManager.find(DbProduitData.class, inserted.getId());
        assertThat(updated.getDateStart()).hasSameTimeAs(avant);
        assertThat(updated.getDateEnd()).hasSameTimeAs(now);
    }

    @Test
    public void findByProduitAndDate() {
        // Given
    	Date now = new Date();
    	Date _3h = Date.from(new Date().toInstant().minusSeconds(3*3600));
    	Date _2h = Date.from(new Date().toInstant().minusSeconds(2*3600));
    	Date _1h = Date.from(new Date().toInstant().minusSeconds(3600));
    	DbProduit produitA = dbProduitRepository.save(new DbProduit("Produit A"));
        dbProduitDataRepository.save(new DbProduitData(produitA, "Mon Produit A", 20, 4000, _3h));
        dbProduitDataRepository.updateDateEnd(produitA.getProduitId(), _1h);
        dbProduitDataRepository.save(new DbProduitData(produitA, "Mon Produit A (a 8 % de tva)", 8, 4500, _1h));

        DbProduitData history = dbProduitDataRepository.findByProduitAndDate(produitA.getProduitId(), _2h);
        assertThat(history).isNotNull();
        assertThat(history.getDesignation()).isEqualTo("Mon Produit A");
        assertThat(history.getTva()).isEqualTo(20);
        assertThat(history.getPrixUnitaireHT()).isEqualTo(4000);

        DbProduitData historyNow = dbProduitDataRepository.findByProduitAndDate(produitA.getProduitId(), now);
        assertThat(historyNow).isNotNull();
        assertThat(historyNow.getDesignation()).isEqualTo("Mon Produit A (a 8 % de tva)");
        assertThat(historyNow.getTva()).isEqualTo(8);
        assertThat(historyNow.getPrixUnitaireHT()).isEqualTo(4500);
    }
    
    @Test
	public void findAllWithoutEndDate() {
		// Given
		Date _3h = Date.from(new Date().toInstant().minusSeconds(3 * 3600));
		Date _1h = Date.from(new Date().toInstant().minusSeconds(3600));
		
		DbProduit produitA = entityManager.persist(new DbProduit("Produit A"));
		DbProduit produitB = entityManager.persist(new DbProduit("Produit B"));
		
		entityManager.persist(new DbProduitData(produitA, "Mon Produit A", 20, 4000, _3h, _1h));
		DbProduitData lastA = entityManager.persist(new DbProduitData(produitA, "Mon Produit A plus recent", 7, 4500, _1h));
		DbProduitData lastB = entityManager.persist(new DbProduitData(produitB, "Mon Produit B", 14, 200, _3h));

		// When
		List<DbProduitData> currentEntries = dbProduitDataRepository.findAllWithoutDateEnd();

		// Then
		assertThat(currentEntries).hasSize(2);
		assertThat(currentEntries).containsExactly(lastA, lastB);
	}
    
}

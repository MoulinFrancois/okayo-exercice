package com.okayo.facturation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.utils.db.DbProduitRepository;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
public class DbProduitRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DbProduitRepository dbProduitRepository;

    @Test
    public void insertion() {
        // Given
        DbProduit produitA = new DbProduit("Produit A");

        // When
        dbProduitRepository.save(produitA);
        DbProduit found = entityManager.find(DbProduit.class, produitA.getProduitId());

        // Then
        assertThat(found.getProduitId()).isEqualTo(produitA.getProduitId());
        assertThat(found.getLabel()).isEqualTo(produitA.getLabel());
    }

    @Test
    public void findAll() {
        // Given
        DbProduit produitA = new DbProduit("Produit A");
        DbProduit produitB = new DbProduit("Produit B");
        DbProduit produitC = new DbProduit("Produit C");
        entityManager.persist(produitA);
        entityManager.persist(produitB);
        entityManager.persist(produitC);
        entityManager.flush();

        // When
        List<DbProduit> found = dbProduitRepository.findAll();

        // Then
        assertThat(found.size()).isEqualTo(3);
        assertThat(found).containsExactly(produitA, produitB, produitC);
    }
    

    @Test
    public void findDbProduitByLabel() {
        DbProduit produitA = new DbProduit("Produit A");
        assertThat(dbProduitRepository.findDbProduitByLabel(produitA.getLabel())).isNotPresent();

        entityManager.persist(produitA);
        entityManager.flush();

        assertThat(dbProduitRepository.findDbProduitByLabel(produitA.getLabel())).isPresent();
    }
    
    @Test
    public void findByProduitId() {
        DbProduit produitA = entityManager.persist(new DbProduit("Produit A"));
        entityManager.flush();

        assertThat(dbProduitRepository.findByProduitId(produitA.getProduitId())).isPresent();
    }
    
}

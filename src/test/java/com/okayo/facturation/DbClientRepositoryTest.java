package com.okayo.facturation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.utils.db.DbClientRepository;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DbClientRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DbClientRepository dbClientRepository;

    @Test
    public void insertion() {
        // Given
        DbClient solly = new DbClient("client26-001", "Solly", "12 rue du test", "75016", "Paris");
        assertThat(entityManager.find(DbClient.class, solly.getCode())).isNull(); // pas en bdd

        // When
        dbClientRepository.save(solly);
        DbClient found = entityManager.find(DbClient.class, solly.getCode());

        // Then
        assertThat(found.getCode()).isEqualTo(solly.getCode());
        assertThat(found.getNom()).isEqualTo(solly.getNom());
        assertThat(found.getLigne()).isEqualTo(solly.getLigne());
        assertThat(found.getCodePostal()).isEqualTo(solly.getCodePostal());
        assertThat(found.getVille()).isEqualTo(solly.getVille());
    }

    @Test
    public void recherche() {
        // Given
        DbClient solly = new DbClient("client26-002", "Azar", "26 rue du test", "75017", "Paris");
        entityManager.persist(solly);
        entityManager.flush();

        // When
        DbClient found = dbClientRepository.findByCode(solly.getCode());
        DbClient inexistant = dbClientRepository.findByCode("inexistant");
        

        // Then
        assertThat(found.getCode()).isEqualTo(solly.getCode());
        assertThat(found.getNom()).isEqualTo(solly.getNom());
        assertThat(found.getLigne()).isEqualTo(solly.getLigne());
        assertThat(found.getCodePostal()).isEqualTo(solly.getCodePostal());
        assertThat(found.getVille()).isEqualTo(solly.getVille());
        
        assertThat(inexistant).isNull();
    }
    
}

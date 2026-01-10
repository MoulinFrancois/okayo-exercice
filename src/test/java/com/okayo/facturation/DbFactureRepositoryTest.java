package com.okayo.facturation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.db.DbFacture;
import com.okayo.facturation.core.utils.db.DbFactureRepository;

@DataJpaTest
public class DbFactureRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DbFactureRepository dbFactureRepository;
    
    @Test
    public void insertion() {
        // Given
    	Date now = new Date();
    	DbClient client = new DbClient("client26-001", "test", "12 rue du test", "75016", "Paris");
    	entityManager.persist(client);
    	
        // When
    	DbFacture inserted = dbFactureRepository.save(new DbFacture(client, now, now));
    	
        // Then
    	assertThat(inserted.getReference()).isNotNull();
    	assertThat(inserted.getClient()).isEqualTo(client);
    	assertThat(inserted.getDateFacturation()).isEqualTo(now);
    	assertThat(inserted.getDateEcheance()).isEqualTo(now);
    }
    
    @Test
    public void findLastDateFacturationBeforeReferenceForClient() {
    	// Given
    	Date now = new Date();
    	Date _1h = Date.from(new Date().toInstant().minusSeconds(3600));
    	DbClient client = new DbClient("client26-001", "test", "12 rue du test", "75016", "Paris");
    	entityManager.persist(client);
    	entityManager.persist(new DbFacture(client, _1h, _1h));
    	DbFacture newerFacture = entityManager.persist(new DbFacture(client, now, now));
    	
    	// When
    	Date found = dbFactureRepository.findLastDateFacturationBeforeReferenceForClient(client.getCode(), newerFacture.getDateFacturation());
    	Date notClient = dbFactureRepository.findLastDateFacturationBeforeReferenceForClient("not client", now);
    	
    	// Then
    	assertThat(found).isSameAs(found);
    	assertThat(notClient).isNull();
    }
}

package com.okayo.facturation.core.utils.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.okayo.facturation.core.model.db.DbClient;

@Repository
public interface DbClientRepository extends JpaRepository<DbClient, String> {
	
	public Optional<DbClient> findByCode(String code);
	
}

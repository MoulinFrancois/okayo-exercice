package com.okayo.facturation.core.utils.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.okayo.facturation.core.model.db.DbClient;

@Repository
public interface DbClientRepository extends JpaRepository<DbClient, String> {
	
	public DbClient findByCode(String code);
	
}

package com.okayo.facturation.core.utils.db;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.okayo.facturation.core.model.db.DbFacture;

@Repository
public interface DbFactureRepository extends JpaRepository<DbFacture, String> {
	
	@Query("select max(f.dateFacturation) from DbFacture f where f.client.code = :clientCode and f.dateFacturation < :referenceDate")
	public Date findLastDateFacturationBeforeReferenceForClient(String clientCode, Date referenceDate);

}

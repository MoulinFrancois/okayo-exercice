package com.okayo.facturation.core.utils.db;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.okayo.facturation.core.model.db.DbProduitSouscrit;

@Repository
public interface DbProduitSouscritRepository extends JpaRepository<DbProduitSouscrit, String> {

	@Query(value = "select ps from DbProduitSouscrit ps where ps.client.code = :clientCode and ps.date <= :dateEnd and ps.date > :dateStart")
	public List<DbProduitSouscrit> findByClientAndDate(String clientCode, Date dateStart, Date dateEnd);
	
}
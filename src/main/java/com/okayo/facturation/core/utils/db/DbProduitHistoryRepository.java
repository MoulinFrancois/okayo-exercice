package com.okayo.facturation.core.utils.db;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.okayo.facturation.core.model.db.DbProduitHistory;

@Repository
public interface DbProduitHistoryRepository extends JpaRepository<DbProduitHistory, String> {
	
	@Modifying
	@Transactional
	@Query(value = "update DbProduitHistory ph set ph.dateEnd = :date where (ph.produit.produitId = :id and ph.dateEnd IS NULL)")
	public int updateProduitHistory(@Param("id") int produitId, @Param("date") Date date);

	@Query(value = "select ph from DbProduitHistory ph where ph.produit.produitId = :id and ph.dateStart <= :date and (ph.dateEnd IS NULL OR ph.dateEnd > :date)")
	public DbProduitHistory findByProduitAndDate(@Param("id") int produitId, @Param("date") Date date);
	
}
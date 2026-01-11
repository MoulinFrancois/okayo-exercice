package com.okayo.facturation.core.utils.db;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.okayo.facturation.core.model.db.DbProduitData;

@Repository
public interface DbProduitDataRepository extends JpaRepository<DbProduitData, String> {
	
	@Modifying
	@Transactional
	@Query(value = "update DbProduitData ph set ph.dateEnd = :date where (ph.produit.produitId = :id and ph.dateEnd IS NULL)")
	public int updateDateEnd(@Param("id") int produitId, @Param("date") Date date);

	@Query(value = "select ph from DbProduitData ph where ph.produit.produitId = :id and ph.dateStart <= :date and (ph.dateEnd IS NULL OR ph.dateEnd > :date)")
	public DbProduitData findByProduitAndDate(@Param("id") int produitId, @Param("date") Date date);
	
	@Query(value = "select ph from DbProduitData ph where ph.dateEnd IS NULL")
	public List<DbProduitData> findAllWithoutDateEnd();
	
}
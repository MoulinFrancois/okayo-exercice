package com.okayo.facturation.core.utils.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.okayo.facturation.core.model.db.DbProduit;

@Repository
public interface DbProduitRepository extends JpaRepository<DbProduit, String> {

	public Optional<DbProduit> findDbProduitByLabel(String label);
	
}

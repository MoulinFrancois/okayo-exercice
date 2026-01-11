package com.okayo.facturation.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.domain.Produit;

@Mapper
public interface ProduitMapper {

	ProduitMapper INSTANCE = Mappers.getMapper(ProduitMapper.class);

	@Mapping(source = "produitId", target = "id")
	public Produit toProduit(DbProduit produit);

	@Mapping(source = "id", target = "produitId")
	public DbProduit toDbProduit(Produit produit);

}
package com.okayo.facturation.services.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.okayo.facturation.core.model.db.DbProduit;
import com.okayo.facturation.core.model.domain.Produit;

public class ProduitMapperTest {
	
	@Test
	public void toDbClient() {
		Produit input = new Produit(1, "Produit A");
		DbProduit output = ProduitMapper.INSTANCE.toDbProduit(input);
	    assertThat(output.getProduitId()).isEqualTo(input.getId());
	    assertThat(output.getLabel()).isEqualTo(input.getLabel());
	}
	
	@Test
	public void toClient() {
		DbProduit input = new DbProduit(1, "Produit A");
		Produit output = ProduitMapper.INSTANCE.toProduit(input);
	    assertThat(output.getId()).isEqualTo(input.getProduitId());
	    assertThat(output.getLabel()).isEqualTo(input.getLabel());
	}

}

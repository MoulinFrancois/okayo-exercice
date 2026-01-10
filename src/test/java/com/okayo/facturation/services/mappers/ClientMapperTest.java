package com.okayo.facturation.services.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.domain.Adresse;
import com.okayo.facturation.core.model.domain.Client;

public class ClientMapperTest {
	
	@Test
	public void toDbClient() {
		Client input = new Client("code", "lenom", new Adresse("laligne", "75014", "PARIS"));
		DbClient output = ClientMapper.INSTANCE.toDbClient(input);
	    assertThat(output.getCode()).isEqualTo(input.getCode());
	    assertThat(output.getNom()).isEqualTo(input.getNom());
	    assertThat(output.getLigne()).isEqualTo(input.getAdresse().getLigne());
	    assertThat(output.getCodePostal()).isEqualTo(input.getAdresse().getCodePostal());
	    assertThat(output.getVille()).isEqualTo(input.getAdresse().getVille());
	}
	
	@Test
	public void toClient() {
		DbClient input = new DbClient("code", "lenom", "laligne", "75014", "PARIS");
		Client output = ClientMapper.INSTANCE.toClient(input);
	    assertThat(output.getCode()).isEqualTo(input.getCode());
	    assertThat(output.getNom()).isEqualTo(input.getNom());
	    assertThat(output.getAdresse().getLigne()).isEqualTo(input.getLigne());
	    assertThat(output.getAdresse().getCodePostal()).isEqualTo(input.getCodePostal());
	    assertThat(output.getAdresse().getVille()).isEqualTo(input.getVille());
	}

}

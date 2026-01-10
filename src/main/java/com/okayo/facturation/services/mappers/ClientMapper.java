package com.okayo.facturation.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.okayo.facturation.core.model.db.DbClient;
import com.okayo.facturation.core.model.domain.Client;

@Mapper
public interface ClientMapper {

	ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

	@Mapping(source = "ligne", target = "adresse.ligne")
	@Mapping(source = "codePostal", target = "adresse.codePostal")
	@Mapping(source = "ville", target = "adresse.ville")
	public Client toClient(DbClient client);

	@Mapping(target = "ligne", source = "adresse.ligne")
	@Mapping(target = "codePostal", source = "adresse.codePostal")
	@Mapping(target = "ville", source = "adresse.ville")
	public DbClient toDbClient(Client client);

}
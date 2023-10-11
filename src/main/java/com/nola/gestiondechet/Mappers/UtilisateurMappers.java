package com.nola.gestiondechet.Mappers;

import com.nola.gestiondechet.Dto.UtilisateurDto;
import com.nola.gestiondechet.Entities.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilisateurMappers {
    UtilisateurDto utilisateurToUtilisateurDto(Utilisateur utilisateur);
    Utilisateur utilisateurDtoToUtilisateur(UtilisateurDto utilisateurDto);
}

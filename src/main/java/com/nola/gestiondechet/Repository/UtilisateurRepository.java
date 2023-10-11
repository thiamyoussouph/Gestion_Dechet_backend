package com.nola.gestiondechet.Repository;

import com.nola.gestiondechet.Entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur,Long> {
    Optional<Utilisateur>  findByemail(String email);
}

package com.nola.gestiondechet.Repository;

import com.nola.gestiondechet.Entities.Roles;
import com.nola.gestiondechet.Enum.TypeDeRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository <Roles, Long> {
    Optional<Roles> findByRole(TypeDeRole role);
}

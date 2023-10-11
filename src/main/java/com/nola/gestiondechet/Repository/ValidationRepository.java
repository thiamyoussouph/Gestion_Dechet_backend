package com.nola.gestiondechet.Repository;

import com.nola.gestiondechet.Services.Validation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation,Long> {

Optional<Validation> findByCode(String code);
}

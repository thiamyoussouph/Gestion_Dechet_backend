package com.nola.gestiondechet.Repository;

import com.nola.gestiondechet.Entities.JWT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<JWT,Long> {

    Optional<JWT> findByValue(String value);
}

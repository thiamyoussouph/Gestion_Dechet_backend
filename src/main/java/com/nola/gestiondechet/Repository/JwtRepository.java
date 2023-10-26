package com.nola.gestiondechet.Repository;
import com.nola.gestiondechet.Entities.JWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<JWT,Long> {

    Optional<JWT> findByValueAndDesactiveAndExpire(String value,boolean deactive,boolean expire);
    @Query("FROM JWT j WHERE j.expire = :expire AND j.desactive = :deactive AND j.utilisateur.email= :email")
    Optional<JWT> findUtilisateurValidToken(String email,boolean deactive,boolean expire);
    @Query("FROM JWT j WHERE j.utilisateur.email= :email")
    Stream<JWT> findUtilisateur(String email);
    @Query("FROM JWT j WHERE j.refreshtoken.valeur= :valeur")
    Optional<JWT> findByRefreshtoken(String valeur);
    void deleteAllByExpireAndDesactive(boolean expire, boolean deactive);
}

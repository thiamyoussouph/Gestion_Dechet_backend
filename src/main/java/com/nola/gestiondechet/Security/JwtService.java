package com.nola.gestiondechet.Security;

import com.nola.gestiondechet.Entities.Utilisateur;
import com.nola.gestiondechet.Services.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class JwtService {
    private UtilisateurService utilisateurService;
    private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";
    public Map<String, String> generate(String username) {
        Utilisateur utilisateur= (Utilisateur) this.utilisateurService.loadUserByUsername(username);

        return this.generateJwt(utilisateur);
    }

    private Map<String, String> generateJwt(Utilisateur utilisateur) {
       final Long currentTime = System.currentTimeMillis();
       final Long expiration = currentTime + + 30 * 60 * 1000;
       final Map<String, Object> claims = Map.of(
               "nom", utilisateur.getNom(),
               Claims.EXPIRATION,new Date(expiration),
               Claims.SUBJECT,utilisateur.getEmail()
         );
     final String bearer=Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expiration))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();



        return Map.of("bearer",bearer);
    }

    private Key getKey() {
        final byte[] decoder= Decoders.BASE64.decode(ENCRIPTION_KEY);
return Keys.hmacShaKeyFor(decoder);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }
    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }
    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

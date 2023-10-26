package com.nola.gestiondechet.Security;

import com.nola.gestiondechet.Entities.JWT;
import com.nola.gestiondechet.Entities.Refreshtoken;
import com.nola.gestiondechet.Entities.Utilisateur;

import com.nola.gestiondechet.Repository.JwtRepository;
import com.nola.gestiondechet.Services.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {
    public static final String BEARER = "bearer";
    public static final String REFRESH = "refresh";
    private UtilisateurService utilisateurService;
    private JwtRepository jwtRepository;



    public Map<String,String>  refreshToken(Map<String, String> refreshTokenRequest) {
     final   JWT jwt= this.jwtRepository.findByRefreshtoken(refreshTokenRequest.get(REFRESH))
                .orElseThrow(() -> new RuntimeException("token invalide"));
      if(jwt.getRefreshtoken().isExpire() || jwt.getRefreshtoken().getExpiration().isBefore(Instant.now())){
          throw new RuntimeException("token invalide");
      }
    Map<String,String>  tokens= this.generate(jwt.getUtilisateur().getEmail());
      this.disableTokens(jwt.getUtilisateur());
      return tokens;
    }


    public JWT tokenbyvalue(String value) {
      return this.jwtRepository.findByValueAndDesactiveAndExpire(
                value,
                false,
              false
              )
              .orElseThrow(()->new RuntimeException("token non trouvé"));
    }
    private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";
    public Map<String, String> generate(String username) {
        Utilisateur utilisateur= (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        this.disableTokens(utilisateur);
         final Map<String, String> jwtMap = this.generateJwt(utilisateur);
        Refreshtoken refreshToken= Refreshtoken.builder()
                .valeur(UUID.randomUUID().toString())
                .expire(false)
                .creation(Instant.now())
                .expiration(Instant.now().plusMillis(30 *60 *1000))
                .build();
       final JWT jwt= JWT.builder()
               .value(jwtMap.get(BEARER))
               .desactive(false)
               .expire(false)
               .utilisateur(utilisateur)
                .refreshtoken(refreshToken)
               .build();
       this.jwtRepository.save(jwt);
         jwtMap.put(REFRESH,refreshToken.getValeur());
        return jwtMap;
    }
   private void disableTokens(Utilisateur utilisateur){
      final List<JWT>  jwtlist= this.jwtRepository.findUtilisateur(utilisateur.getEmail()).peek(
                jwt -> {
                    jwt.setDesactive(true);
                    jwt.setExpire(true);
                })
                .collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtlist);

    }

    private Map<String, String> generateJwt(Utilisateur utilisateur) {
       final Long currentTime = System.currentTimeMillis();
       final Long expiration = currentTime +   60 * 1000;
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



        return Map.of(BEARER,bearer);
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


    public void deconnexion() {
        Utilisateur utilisateur= (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    JWT jwt=    this.jwtRepository.findUtilisateurValidToken(utilisateur.getEmail(),false,
                false).orElseThrow(() -> new RuntimeException("token non trouvé"));
   jwt.setExpire(true);
    jwt.setDesactive(true);
    this.jwtRepository.save(jwt);


    }
    //@Scheduled(cron = "0 */1 * * * *")
    @Scheduled(cron = "@daily")
    public void removeUselessJwt(){
        log.info("suppression des jwt expirés a {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDesactive(true,true);
    }
}

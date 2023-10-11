package com.nola.gestiondechet.Controller;

import com.nola.gestiondechet.Dto.UtilisateurDto;
import com.nola.gestiondechet.Security.JwtService;
import com.nola.gestiondechet.Services.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisationController {
 private AuthenticationManager authenticationManager;
    private final UtilisateurService utilisateurService;
    private JwtService jwtService;

    @PostMapping(path = "/inscription")
    public void inscription(@RequestBody UtilisateurDto utilisateurDto) {
        log.info("inscription");
        utilisateurService.inscription(utilisateurDto);
    }

    @PostMapping(path = "/activation")
    public void validation(@RequestBody Map<String, String> activation) {
        utilisateurService.activation(activation);
    }


    @PostMapping(path = "/connexion")
    public Map<String,String>connexion(@RequestBody AuthentificationDto authentificationDto){
        final Authentication authenticate =authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authentificationDto.username(),authentificationDto.password())
);
if(authenticate.isAuthenticated()){
   return this.jwtService.generate(authentificationDto.username());
}
        return null;
    }
    @GetMapping("/utilisateurCourant")
    public String getUtilisateurCourant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return "Utilisateur courant: " + authentication.getName();
        }
        return "Aucun utilisateur n'est actuellement authentifié";
    }
}

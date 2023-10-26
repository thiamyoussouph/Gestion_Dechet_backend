package com.nola.gestiondechet.Controller;

import com.nola.gestiondechet.Dto.UtilisateurDto;
import com.nola.gestiondechet.Entities.Utilisateur;
import com.nola.gestiondechet.Security.JwtService;
import com.nola.gestiondechet.Services.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController

public class UtilisationController {
 private AuthenticationManager authenticationManager;
    private final UtilisateurService utilisateurService;
    private JwtService jwtService;

    @PostMapping(path = "/inscription")
    public void inscription(@RequestBody  Utilisateur utilisateur) {
        log.info("inscription");
        utilisateurService.inscription(utilisateur);
    }

    @PostMapping(path = "/activation")
    public void validation(@RequestBody Map<String, String> activation) {
        utilisateurService.activation(activation);
    }

    @PostMapping(path = "/deconnexion")
    public void deconexion() {
        this.jwtService.deconnexion();
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
    @GetMapping("/current-user")
    public Utilisateur  getCurrentUser(Principal principal) {

        return (Utilisateur) utilisateurService.loadUserByUsername(principal.getName());
    }
    @PostMapping(path = "/refresh-token")
    public @ResponseBody Map<String,String> refreshToken(@RequestBody Map<String,String> refreshTokenRequest){
        return this.jwtService.refreshToken(refreshTokenRequest);
    }

}

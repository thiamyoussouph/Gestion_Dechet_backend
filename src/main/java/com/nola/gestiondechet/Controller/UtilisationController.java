package com.nola.gestiondechet.Controller;

import com.nola.gestiondechet.Dto.UtilisateurDto;
import com.nola.gestiondechet.Services.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisationController {
 private AuthenticationManager authenticationManager;
    private final UtilisateurService utilisateurService;

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
        return null;
    }
}

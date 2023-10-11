package com.nola.gestiondechet.Services;

import com.nola.gestiondechet.Dto.UtilisateurDto;
import com.nola.gestiondechet.Entities.Roles;
import com.nola.gestiondechet.Entities.Utilisateur;
import com.nola.gestiondechet.Enum.TypeDeRole;
import com.nola.gestiondechet.Mappers.UtilisateurMappers;
import com.nola.gestiondechet.Repository.UtilisateurRepository;
import com.nola.gestiondechet.Services.Implement.Usereservice;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Service
@Transactional
public class UtilisateurService implements Usereservice, UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationService validationService;
    private final UtilisateurMappers utilisateurMappers;

    @Override
    public void inscription(UtilisateurDto utilisateurDto) {
        if (!utilisateurDto.getEmail().contains("@") || !utilisateurDto.getEmail().contains(".")) {
            throw new RuntimeException("email invalide");
        }

        if (utilisateurRepository.findByemail(utilisateurDto.getEmail()).isPresent()) {
            throw new RuntimeException("email existe deja");
        }

        Utilisateur utilisateur = utilisateurMappers.utilisateurDtoToUtilisateur(utilisateurDto);
        String mdpCripte = bCryptPasswordEncoder.encode(utilisateurDto.getMotDePasse());

        utilisateur.setMotDePasse(mdpCripte);
        Roles roleUtilisateur = new Roles();
        roleUtilisateur.setRole(TypeDeRole.ADMIN);
        utilisateur.setRole(roleUtilisateur);

        utilisateur = utilisateurRepository.save(utilisateur);
        validationService.enregistrerValidation(utilisateur);
    }

    @Override
    public void activation(Map<String, String> activation) {
        // Note: La classe Validation n'a pas été fournie, alors assurez-vous que cette section fonctionne comme prévu.
        Validation validation = validationService.lireEnFonctionDuCode(activation.get("code"));

        if (validation.getUtilisateur() == null) {
            throw new RuntimeException("votre code invalide");
        }
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("votre code a expiré");
        }

        utilisateurRepository.findById(validation.getUtilisateur().getId()).ifPresent(utilisateur -> {
            utilisateur.setActif(true);
            utilisateurRepository.save(utilisateur);
        });
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return this.utilisateurRepository
               .findByemail(username).orElseThrow(()->new UsernameNotFoundException(
                       "Aucun utilisateur ne corespond à cet identifiant"
               ));

    }
}

package com.nola.gestiondechet.Services;

import com.nola.gestiondechet.Entities.Roles;
import com.nola.gestiondechet.Entities.Utilisateur;
import com.nola.gestiondechet.Enum.TypeDeRole;
import com.nola.gestiondechet.Mappers.UtilisateurMappers;
import com.nola.gestiondechet.Repository.JwtRepository;
import com.nola.gestiondechet.Repository.RoleRepository;
import com.nola.gestiondechet.Repository.UtilisateurRepository;
import com.nola.gestiondechet.Services.Implement.Usereservice;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilisateurService implements  UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationService validationService;
    private final UtilisateurMappers utilisateurMappers;
    private final RoleRepository roleRepository;
    private JwtRepository JwtRepository;


    public void inscription(Utilisateur utilisateur) {
        // Vérifiez que l'email est valide
        if (!utilisateur.getEmail().contains("@") || !utilisateur.getEmail().contains(".")) {
            throw new RuntimeException("email invalide");
        }

        // Vérifiez que l'email n'existe pas déjà
        if (utilisateurRepository.findByemail(utilisateur.getEmail()).isPresent()) {
            throw new RuntimeException("email existe deja");
        }

        // Cryptez le mot de passe
        String mdpCripte = bCryptPasswordEncoder.encode(utilisateur.getMotDePasse());

        utilisateur.setMotDePasse(mdpCripte);

        // Définir le rôle de l'utilisateur
        utilisateur.setMotDePasse(mdpCripte);
        Roles roleUtilisateur = new Roles();
        roleUtilisateur.setRole(TypeDeRole.ADMIN);
        utilisateur.setRole(roleUtilisateur);

        // Enregistrez l'utilisateur
        utilisateur = utilisateurRepository.save(utilisateur);

        // Enregistrez la validation
        validationService.enregistrerValidation(utilisateur);
    }


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
    public void changeUserRole(String userEmail, TypeDeRole newRole) {
        Utilisateur utilisateur = utilisateurRepository.findByemail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        Roles role = roleRepository.findByRole(newRole)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        utilisateur.setRole((Roles) Collections.singleton(role));
        utilisateurRepository.save(utilisateur);
    }


}

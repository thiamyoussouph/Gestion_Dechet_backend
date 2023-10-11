package com.nola.gestiondechet.Services;

import com.nola.gestiondechet.Entities.Utilisateur;
import com.nola.gestiondechet.Repository.ValidationRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@AllArgsConstructor
public class ValidationService {
    private ValidationRepository validationRepository;
    private NotificationService notificationService;
    public void enregistrerValidation(Utilisateur utilisateur) {
        Validation validation=new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation=Instant.now();
        validation.setCreation(creation);
        Instant expiration=creation.plus(10,MINUTES);
        validation.setExpiration(expiration);
        Random random=new Random();
        int randomInteger=random.nextInt(999999);
        String code=String.format("%06d",randomInteger);
        validation.setCode(code);
  this.validationRepository.save(validation);
  this.notificationService.EnvoyerEmail(validation);
    }
    public Validation lireEnFonctionDuCode(String code) {
        return this.validationRepository.findByCode(code).orElseThrow(()->new RuntimeException("code invalide"));

    }
}


package com.nola.gestiondechet.Services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor

@Service
public class NotificationService {
    JavaMailSender javaMailSender;
    public void EnvoyerEmail(Validation validation) {
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom("thiamasjo@gmail.com");
        mailMessage.setTo(validation.getUtilisateur().getEmail());
        mailMessage.setSubject("Validation de compte");
        String tesxt = String.format("Bonjour %s,\n" +
                        "votre code d activation est %s.\n" ,
                validation.getUtilisateur().getNom(), validation.getCode());
        mailMessage.setText(tesxt);
        javaMailSender.send(mailMessage);
    }
}
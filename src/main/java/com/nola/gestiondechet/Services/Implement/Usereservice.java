package com.nola.gestiondechet.Services.Implement;

import com.nola.gestiondechet.Dto.UtilisateurDto;
import com.nola.gestiondechet.Entities.Utilisateur;

import java.util.Map;

public interface Usereservice {
    void inscription(UtilisateurDto utilisateurDto);
    void activation(Map<String, String> activation);

}

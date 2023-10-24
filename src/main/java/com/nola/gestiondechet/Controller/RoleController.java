package com.nola.gestiondechet.Controller;

import com.nola.gestiondechet.Enum.TypeDeRole;
import com.nola.gestiondechet.Services.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

@AllArgsConstructor
public class RoleController {
    private final UtilisateurService utilisateurService;

    @PutMapping("/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserRole(@RequestParam String userEmail,
                                            @RequestParam TypeDeRole newRole) {
        utilisateurService.changeUserRole(userEmail, newRole);
        return new ResponseEntity<>("Le rôle de l'utilisateur a été modifié", HttpStatus.OK);
    }
}

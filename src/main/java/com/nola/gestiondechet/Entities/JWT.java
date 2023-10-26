package com.nola.gestiondechet.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data

public class JWT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    private boolean desactive;
    private boolean expire;
    @OneToOne(cascade={CascadeType.PERSIST,CascadeType.REMOVE})
 private  Refreshtoken refreshtoken;
    @ManyToOne(cascade={CascadeType.DETACH,CascadeType.MERGE})
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

}

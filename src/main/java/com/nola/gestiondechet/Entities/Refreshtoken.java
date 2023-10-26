package com.nola.gestiondechet.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data

public class Refreshtoken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String valeur;
    private boolean expire;
    private Instant expiration;
    private Instant creation;


}

package com.nola.gestiondechet.Entities;

import com.nola.gestiondechet.Enum.TypeDeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    @Enumerated(EnumType.STRING)
    private TypeDeRole role;

}

package tn.iit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Table(name = "compteur")
@Data
public class Compteur {

    @Id
    private String prefixe; 

    @Column(name = "valeur_actuelle", nullable = false)
    private Integer valeurActuelle=0 ;

    @Column(name = "annee_courante", nullable = false)
    private Integer anneeCourante = LocalDate.now().getYear();
}
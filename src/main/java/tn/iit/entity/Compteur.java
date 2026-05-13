package tn.iit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "compteur")
@Data
public class Compteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 100)
    private String prefixe;
    
    @Column(name = "valeur_actuelle", nullable = false)
    private Integer valeurActuelle = 1;
    
    private String description;
    private String format;
    
    @Column(name = "annee_courante")
    private Integer anneeCourante = java.time.LocalDate.now().getYear();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
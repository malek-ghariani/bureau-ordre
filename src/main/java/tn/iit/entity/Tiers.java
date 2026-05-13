package tn.iit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tiers")
@Data
public class Tiers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    private String telephone;
    private String adresse;
    private String email;
    
    
    @Column(name = "type", nullable = false)
    private String type = "PERSONNE";
    
    private String nomContact;
   
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "tiers")
    private List<CourrierEntrant> courriersEntrants = new ArrayList<>();
    
    @OneToMany(mappedBy = "tiers")
    private List<CourrierSortant> courriersSortants = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
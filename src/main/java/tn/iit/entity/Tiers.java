package tn.iit.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

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
    
    @JsonIgnore 
    @OneToMany(mappedBy = "expediteur")
    private List<CourrierEntrant> courriersEntrants = new ArrayList<>();
    
    @JsonIgnore 
    @OneToMany(mappedBy = "destinataire")
    private List<CourrierSortant> courriersSortants = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
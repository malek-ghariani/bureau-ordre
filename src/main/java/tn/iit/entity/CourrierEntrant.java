package tn.iit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "courrier_entrant")
@Data
public class CourrierEntrant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numeroOrdre;
    
    @Column(nullable = false)
    private LocalDate dateReception;
    
    @Column(nullable = false, updatable = false)
    private LocalDate dateSaisie;
    
    private String typeDocument;
    private String reference;
    private String nature;
    private String expediteur;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_reception", nullable = false)
    private ModeReception modeReception;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCourrier statut = StatutCourrier.NOUVEAU;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatCourrier etat = EtatCourrier.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priorite priorite = Priorite.NORMALE;
    
    
    
    @ManyToOne
    @JoinColumn(name = "tiers_id")
    private Tiers tiers;
    
    @ManyToOne
    @JoinColumn(name = "departement_destinataire_code")
    private Departement departementDestinataire;
    
    @OneToMany(mappedBy = "courrierEntrant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PieceJointe> piecesJointes = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;
    
    @OneToMany(mappedBy = "courrierEntrant")
    @JsonIgnore
    private List<TransmissionCourrier> transmissions = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
    	dateSaisie = LocalDate.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

	
}
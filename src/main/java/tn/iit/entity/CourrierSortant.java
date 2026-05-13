package tn.iit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "courrier_sortant")
@Data
public class CourrierSortant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numeroOrdre;
    
    @Column(nullable = false)
    private LocalDate dateEmission;
    
    @Column(nullable = false, updatable = false)
    private LocalDate dateSaisie;
    
    private String typeDocument;
    private String reference;
    private String nature;
    private String destinataire;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_expedition", nullable = false)
    private ModeExpedition modeExpedition ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCourrier statut = StatutCourrier.NOUVEAU;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatCourrier etat = EtatCourrier.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priorite priorite = Priorite.NORMALE;
    
    
    private String adresseLivraison;
    private String emailDestinataire;
    
    @ManyToOne
    @JoinColumn(name = "tiers_id")
    private Tiers tiers;
    
    @ManyToOne
    @JoinColumn(name = "departement_emetteur_code")
    private Departement departementEmetteur;
    
    @OneToMany(mappedBy = "courrierSortant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PieceJointe> piecesJointes = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "courrierSortant")
    @JsonIgnore
    private List<TransmissionCourrier> transmissions = new ArrayList<>();
    
    @Column(name = "date_expedition")
    private LocalDateTime dateExpedition;
    
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
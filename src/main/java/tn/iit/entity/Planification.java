package tn.iit.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Employe destinataire;

    @Enumerated(EnumType.STRING)
    private StatutPlanification statut;
    
    @Enumerated(EnumType.STRING)
    private ResultatTraitement resultat;
    // 👇 message libre
    private String message;

    private LocalDateTime dateEcheance;

   

    // =========================
    // 🔥 SOURCE DE LA PLANIFICATION
    // =========================
    // COURRIER ou MANUEL
    @Enumerated(EnumType.STRING)
    private TypeSourcePlanification typeSource;

    // =========================
    // COURRIER OPTIONNEL
    // =========================
    @ManyToOne(optional = true)
    @JoinColumn(name = "courrier_entrant_id")
    private CourrierEntrant courrierEntrant;

    @ManyToOne(optional = true)
    @JoinColumn(name = "courrier_sortant_id")
    private CourrierSortant courrierSortant;
    


    // =========================
    // PIÈCES JOINTES
    // =========================
    @OneToMany
    @JoinColumn(name = "planification_id")
    private List<PieceJointe> piecesJointes = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "transmission_id")
    private TransmissionCourrier transmission;
}
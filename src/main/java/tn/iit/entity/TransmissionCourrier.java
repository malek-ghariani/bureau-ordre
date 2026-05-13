package tn.iit.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "transmission_courrier")
@Data
public class TransmissionCourrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📌 Un seul des deux doit être rempli selon le type
    @ManyToOne
    @JoinColumn(name = "courrier_entrant_id")
    private CourrierEntrant courrierEntrant;

    @ManyToOne
    @JoinColumn(name = "courrier_sortant_id")
    private CourrierSortant courrierSortant;

    @ManyToOne
    @JoinColumn(name = "expediteur_id", nullable = false)
    @JsonIgnoreProperties({"password"})
    private Employe expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    @JsonIgnoreProperties({"password"})
    private Employe destinataire;
    
    private LocalDateTime dateEnvoi;
    private LocalDateTime dateLecture;
    @OneToOne(mappedBy = "transmission")
    private Planification planification;

    @PrePersist
    protected void onCreate() {
        dateEnvoi = LocalDateTime.now();
    }

    private String message;     // message envoyé au destinataire
    
    
}
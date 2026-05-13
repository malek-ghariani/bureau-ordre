package tn.iit.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "piece_jointe")
@Data
public class PieceJointe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomFichier;

    @Column(nullable = false)
    private String cheminStockage;

    private String description;

    private Long taille;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courrier_entrant_id")
    @JsonIgnore
    private CourrierEntrant courrierEntrant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courrier_sortant_id")
    @JsonIgnore
    private CourrierSortant courrierSortant;
    
    @ManyToOne
    @JoinColumn(name = "planification_id", nullable = true)
    private Planification planification;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private Employe uploadedBy;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
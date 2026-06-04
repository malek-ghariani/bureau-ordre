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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "courrier_entrant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

 @Column(name = "date_archivage")
 private LocalDateTime dateArchivage;

 
 @ManyToOne
 @JoinColumn(name = "tiers_id")
 private Tiers expediteur;

 

 @OneToMany(mappedBy = "courrierEntrant", cascade = CascadeType.ALL, orphanRemoval = true)
 @JsonIgnore
 private List<PieceJointe> piecesJointes = new ArrayList<>();

 @OneToMany(mappedBy = "courrierEntrant", cascade = CascadeType.ALL)
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

 @Override
 public boolean equals(Object o) {
     if (this == o) return true;
     if (!(o instanceof CourrierEntrant)) return false;
     CourrierEntrant that = (CourrierEntrant) o;
     return id != null && id.equals(that.id);
 }

 @Override
 public int hashCode() {
     return getClass().hashCode();
 }
}
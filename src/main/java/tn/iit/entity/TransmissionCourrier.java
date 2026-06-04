package tn.iit.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Check;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transmission_courrier")
@Check(constraints =
    "(courrier_entrant_id IS NOT NULL AND courrier_sortant_id IS NULL) OR " +
    "(courrier_entrant_id IS NULL AND courrier_sortant_id IS NOT NULL)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransmissionCourrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courrier_entrant_id")
    private CourrierEntrant courrierEntrant;

    @ManyToOne
    @JoinColumn(name = "courrier_sortant_id")
    private CourrierSortant courrierSortant;

    // Employé qui reçoit
    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Employe destinataire;

    private String message;  // instructions du responsable BO

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;  // quand l'employé a ouvert

    // Toujours une seule planification créée automatiquement à l'envoi
    @OneToOne(mappedBy = "transmission", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Planification planification;

    @PrePersist
    protected void onCreate() {
        dateEnvoi = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransmissionCourrier)) return false;
        TransmissionCourrier that = (TransmissionCourrier) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
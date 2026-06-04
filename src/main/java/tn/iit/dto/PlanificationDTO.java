package tn.iit.dto;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanificationDTO {

    private Long id;

    @NotNull(message = "La date d'échéance est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateEcheance;

    private String statut;               // lecture uniquement
    private String resultat;             // lecture uniquement
    private String commentaireResultat;  // réponse de l'employé

    // Destinataire (lecture uniquement, déduit de la transmission)
    private Long destinataireId;
    private String destinataireNom;

    // Transmission (lecture uniquement)
    private Long transmissionId;
    private String transmissionMessage;  // instruction du responsable BO

    private Long courrierEntrantId;
    private Long courrierSortantId;
    private List<Long> piecesJointesIds;
    private List<String> piecesJointesNoms;
   

    private LocalDateTime createdAt;
}
package tn.iit.dto;
import lombok.*;
import tn.iit.entity.TypeSourcePlanification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanificationDTO {

    private Long id;

    // 👇 destinataire
    private Long destinataireId;
    private String destinataireNom;

    // 👇 contenu
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateEcheance;

    // 🟦 statut de traitement (EN_ATTENTE, TERMINE, etc.)
    private String statut;

    // 🟩 resultat (ACCEPTE, REFUSE, EN_COURS...)
    private String resultat;



    // 👇 courrier optionnel
    private Long courrierEntrantId;
    private Long courrierSortantId;
    private String transmissionMessage;

    // 👇 transmission d’origine
    private Long transmissionId;

    // 👇 pièces jointes
    private List<Long> piecesJointesIds;
    private List<String> piecesJointesNoms;
    private TypeSourcePlanification typeSource;
}
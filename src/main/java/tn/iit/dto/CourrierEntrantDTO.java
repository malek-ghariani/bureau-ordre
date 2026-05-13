package tn.iit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CourrierEntrantDTO {

    private Long id;
    private String numeroOrdre;

    @NotNull(message = "La date de réception est obligatoire")
    private LocalDate dateReception;

    private LocalDate dateSaisie;

    private String typeDocument;
    private String reference;
    private String nature;
    private String expediteur;

    // ENUMS → en String
    private String modeReception;
    private String statut;   // NOUVEAU, EN_COURS, TRAITE...
    private String etat;     // ACTIVE, ARCHIVE, SUPPRIME
    private String priorite;

    private String detailsLivraison;

    private Long tiersId;
    private String nomTiers;

    private String departementDestinataireCode;
    private String nomDepartementDestinataire;

    private String createdByMatricule;
}
package tn.iit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourrierSortantDTO {

    private Long id;
    private String numeroOrdre;

    @NotNull(message = "La date d'émission est obligatoire")
    private LocalDate dateEmission;

    private LocalDate dateSaisie;

    private String typeDocument;
    private String reference;
    private String nature;
    private String destinataire;

    // ENUMS
    private String modeExpedition;
    private String statut;
    private String etat;
    private String priorite;

    private String adresseLivraison;
    private String emailDestinataire;

    private Long tiersId;
    private String nomTiers;

    private String departementEmetteurCode;
    private String nomDepartementEmetteur;

    private String createdByMatricule;
}
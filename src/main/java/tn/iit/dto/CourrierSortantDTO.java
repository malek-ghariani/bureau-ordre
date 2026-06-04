package tn.iit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourrierSortantDTO {

    private Long id;
    private String numeroOrdre;

    @NotNull(message = "La date d'émission est obligatoire")
    private LocalDate dateEmission;

    private LocalDate dateSaisie;        // lecture uniquement
    private String typeDocument;
    private String reference;
    private String nature;

    // Enums en String
    private String modeExpedition;
    private String statut;
    private String etat;
    private String priorite;

    private LocalDateTime dateArchivage;  // lecture uniquement
    private LocalDate dateExpedition; // lecture uniquement

    // Destinataire externe (Tiers)
    private Long destinataireId;          // envoyé par le client
    private String nomDestinataire;       // lecture uniquement
  

   
}
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

    private LocalDate dateSaisie;      // lecture uniquement, jamais envoyé par le client
    private String typeDocument;
    private String reference;
    private String nature;

    // Enums en String
    private String modeReception;
    private String statut;
    private String etat;
    private String priorite;

    private LocalDateTime dateArchivage; // lecture uniquement

    // Expéditeur externe (Tiers)
    private Long expediteurId;          // envoyé par le client pour créer/modifier
    private String nomExpediteur;       // retourné en lecture uniquement

  
}
package tn.iit.dto;

import java.time.LocalDate;

import lombok.Data;


@Data
public class CourrierDTO {

    private Long id;
    private String numeroOrdre;
    private LocalDate date;

    private String type; // ENTRANT / SORTANT

    private String reference;
    private String nature;

    private String expediteur; // ou destinataire

    private String statut;
    private String etat;      // 🔥 AJOUT IMPORTANT
    private String priorite;  // 🔥 utile pour UI
}
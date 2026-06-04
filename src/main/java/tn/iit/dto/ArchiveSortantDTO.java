package tn.iit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
@Data
public class ArchiveSortantDTO {

    private Long id;
    private String numeroOrdre;
    private String reference;
    private String nature;

    private LocalDate dateEmission;
    private LocalDate dateExpedition;
    private LocalDateTime dateArchivage;

    private String statut;
    private String etat;
    private String priorite;

    private String destinataire;
    private List<TransmissionArchiveDTO> transmissions;
    private List<PieceJointeDTO> piecesJointes;
}
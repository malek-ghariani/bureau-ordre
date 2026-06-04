package tn.iit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ArchiveEntrantDTO {
    private Long id;
    private String numeroOrdre;
    private String reference;
    private String expediteur;
    private LocalDate dateReception;
    private LocalDateTime dateArchivage;
    private String nature;
    private String priorite;
    private List<TransmissionArchiveDTO> transmissions;
    private List<PieceJointeDTO> piecesJointes;
}

package tn.iit.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TransmissionRequest {
    private Long courrierId;
    private String type;
    private Long destinataireId;
    private String message;
    private LocalDateTime dateEcheance;
    private List<Long> piecesJointesIds;
}
package tn.iit.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TransmissionRequest {
    private Long courrierId;
    private String type;
    private Long destinataireId;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateEcheance;
    private List<Long> piecesJointesIds;
}
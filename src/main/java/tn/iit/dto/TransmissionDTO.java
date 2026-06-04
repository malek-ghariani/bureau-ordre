package tn.iit.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;




@Data
public class TransmissionDTO {

    private Long id;

    // message envoyé par l'expéditeur
    private String message;

    // 📌 type de courrier : ENTRANT / SORTANT
    private String type;

    // 📌 identifiant du courrier lié
    private Long courrierId;

   
    private Long destinataireId;

    // 📅 dates utiles
    private LocalDateTime dateEnvoi;
    private LocalDateTime dateLecture;
    
    
}
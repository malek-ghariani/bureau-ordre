package tn.iit.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransmissionArchiveDTO {
    private Long id;
    private String message;           
    private LocalDateTime dateEnvoi;
    private String expediteurNom;
    private String destinataireNom;
    private String resultat;          
    private String reponseEmploye; 

}
package tn.iit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TiersDTO {
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    private String telephone;
    private String adresse;
    private String email;
    private String fax;
    private String type;
    private String nomContact;
    private String ice;
    private String ifisc;
    private String rc;
}
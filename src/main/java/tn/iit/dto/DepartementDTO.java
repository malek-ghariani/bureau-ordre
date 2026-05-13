package tn.iit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartementDTO {
    @NotBlank(message = "Le code est obligatoire")
    private String code;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    private String chefDepartement;
    private String email;
    private String telephone;
}
package tn.iit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartementDTO {

    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    // on stocke l'id du chef (recommandé)
    private Long chefDepartementId;

    private String email;
    private String telephone;
}
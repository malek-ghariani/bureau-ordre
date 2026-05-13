package tn.iit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import tn.iit.entity.PosteEmploye;
import tn.iit.entity.RoleEmploye;

@Data
public class EmployeDTO {
    private Long id;
    
    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
    
    private String telephone;
    private PosteEmploye poste;
    private String departementCode;
    private String nomDepartement;
    private RoleEmploye role;
    private boolean enabled;
}
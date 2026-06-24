package tn.iit.mapper;

import org.springframework.stereotype.Component;

import tn.iit.dto.DepartementDTO;
import tn.iit.entity.Departement;
import tn.iit.entity.Employe;

@Component
public class DepartementMapper {

    public DepartementDTO toDTO(Departement departement) {
        if (departement == null) return null;

        DepartementDTO dto = new DepartementDTO();
        dto.setCode(departement.getCode());
        dto.setNom(departement.getNom());

        if (departement.getChefDepartement() != null) {
            dto.setChefDepartementId(departement.getChefDepartement().getId());
            dto.setChefDepartementNom(departement.getChefDepartement().getNom());
        }

        dto.setEmail(departement.getEmail());
        dto.setTelephone(departement.getTelephone());

        return dto;
    }

    public Departement toEntity(DepartementDTO dto, Employe chef) {
        if (dto == null) return null;

        Departement dep = new Departement();
        dep.setCode(dto.getCode());
        dep.setNom(dto.getNom());
        dep.setChefDepartement(chef); 
        dep.setEmail(dto.getEmail());
        dep.setTelephone(dto.getTelephone());
        

        return dep;
    }
}
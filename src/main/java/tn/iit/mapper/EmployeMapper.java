package tn.iit.mapper;

import tn.iit.dto.EmployeDTO;
import tn.iit.entity.Employe;
import tn.iit.entity.Departement;
import org.springframework.stereotype.Component;

@Component
public class EmployeMapper {

    public EmployeDTO toDTO(Employe employe) {
        if (employe == null) return null;

        EmployeDTO dto = new EmployeDTO();
        dto.setId(employe.getId());
        dto.setMatricule(employe.getMatricule());
        dto.setNom(employe.getNom());
        dto.setEmail(employe.getEmail());
        dto.setTelephone(employe.getTelephone());
        dto.setPoste(employe.getPoste());
        dto.setRole(employe.getRole());
        dto.setEnabled(employe.isEnabled());
        

        if (employe.getDepartement() != null) {
            dto.setDepartementCode(employe.getDepartement().getCode());
            dto.setNomDepartement(employe.getDepartement().getNom());
        }

        return dto;
    }

    public Employe toEntity(EmployeDTO dto) {
        if (dto == null) return null;

        Employe employe = new Employe();
        employe.setId(dto.getId());
        employe.setMatricule(dto.getMatricule());
        employe.setNom(dto.getNom());
        employe.setEmail(dto.getEmail());
        employe.setTelephone(dto.getTelephone());
        employe.setPoste(dto.getPoste());
        employe.setRole(dto.getRole());
        employe.setEnabled(dto.isEnabled());
        employe.setPassword(dto.getPassword()); 

        
        if (dto.getDepartementCode() != null) {
            Departement dep = new Departement();
            dep.setCode(dto.getDepartementCode());
            employe.setDepartement(dep);
        }

        return employe;
    }
}

package tn.iit.mapper;

import org.springframework.stereotype.Component;

import tn.iit.dto.TiersDTO;
import tn.iit.entity.Tiers;

@Component
public class TiersMapper {
    public TiersDTO toDTO(Tiers tiers) {
        TiersDTO dto = new TiersDTO();
        dto.setId(tiers.getId());
        dto.setNom(tiers.getNom());
        dto.setTelephone(tiers.getTelephone());
        dto.setAdresse(tiers.getAdresse());
        dto.setEmail(tiers.getEmail());
        dto.setType(tiers.getType());
        dto.setNomContact(tiers.getNomContact());
        return dto;
    }

    public Tiers toEntity(TiersDTO dto) {
        Tiers tiers = new Tiers();
        tiers.setId(dto.getId());
        tiers.setNom(dto.getNom());
        tiers.setTelephone(dto.getTelephone());
        tiers.setAdresse(dto.getAdresse());
        tiers.setEmail(dto.getEmail());
        tiers.setType(dto.getType());
        tiers.setNomContact(dto.getNomContact());
        return tiers;
    }
}
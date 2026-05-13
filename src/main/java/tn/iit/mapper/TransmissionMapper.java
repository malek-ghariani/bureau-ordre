package tn.iit.mapper;




import org.springframework.stereotype.Component;


import tn.iit.dto.TransmissionDTO;

import tn.iit.entity.TransmissionCourrier;

@Component
public class TransmissionMapper {

    // =========================
    // ENTITY → DTO
    // =========================
    public static TransmissionDTO toDTO(TransmissionCourrier t) {

        if (t == null) return null;

        TransmissionDTO dto = new TransmissionDTO();

        dto.setId(t.getId());
        dto.setMessage(t.getMessage());

        // =========================
        // 📨 TYPE + COURRIER ID
        // =========================
        if (t.getCourrierEntrant() != null) {
            dto.setCourrierId(t.getCourrierEntrant().getId());
            dto.setType("ENTRANT");

        } else if (t.getCourrierSortant() != null) {
            dto.setCourrierId(t.getCourrierSortant().getId());
            dto.setType("SORTANT");
        }

        // =========================
        // 👤 EXPEDITEUR / DESTINATAIRE
        // =========================
        dto.setExpediteurId(
                t.getExpediteur() != null ? t.getExpediteur().getId() : null
        );

        dto.setDestinataireId(
                t.getDestinataire() != null ? t.getDestinataire().getId() : null
        );

        // =========================
        // 📅 DATES
        // =========================
        dto.setDateEnvoi(t.getDateEnvoi());
        dto.setDateLecture(t.getDateLecture());

        // =========================
        // 📎 PIECES JOINTES
        // =========================
        

        return dto;
    }
}
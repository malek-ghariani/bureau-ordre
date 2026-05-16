package tn.iit.mapper;

import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

import tn.iit.dto.PlanificationDTO;
import tn.iit.entity.PieceJointe;
import tn.iit.entity.Planification;

@Component
public class PlanificationMapper {

    public static PlanificationDTO toDTO(Planification p) {

        PlanificationDTO dto = new PlanificationDTO();

        dto.setId(p.getId());
        dto.setMessage(p.getMessage());
        dto.setDateEcheance(p.getDateEcheance());

        // 🎯 Enums convertis en String
        if (p.getStatut() != null)
            dto.setStatut(p.getStatut().name());

        if (p.getResultat() != null)
            dto.setResultat(p.getResultat().name());

        dto.setTypeSource(p.getTypeSource());

        // =========================
        // 👤 DESTINATAIRE
        // =========================
        if (p.getDestinataire() != null) {
            dto.setDestinataireId(p.getDestinataire().getId());
            dto.setDestinataireNom(p.getDestinataire().getNom());
        }

        // =========================
        // 📩 COURRIER ENTRANT
        // =========================
        if (p.getCourrierEntrant() != null) {
            dto.setCourrierEntrantId(p.getCourrierEntrant().getId());
        }

        // =========================
        // 📤 COURRIER SORTANT
        // =========================
        if (p.getCourrierSortant() != null) {
            dto.setCourrierSortantId(p.getCourrierSortant().getId());
        }

        // =========================
        // 🔗 TRANSMISSION ORIGINE
        // =========================
        if (p.getTransmission() != null) {
            dto.setTransmissionId(p.getTransmission().getId());
            dto.setTransmissionMessage(p.getTransmission().getMessage()); 
        }

        // =========================
        // 📎 PIÈCES JOINTES
        // =========================
        if (p.getPiecesJointes() != null) {

            dto.setPiecesJointesIds(
                    p.getPiecesJointes().stream()
                            .map(PieceJointe::getId)
                            .collect(Collectors.toList())
            );

            dto.setPiecesJointesNoms(
                    p.getPiecesJointes().stream()
                            .map(PieceJointe::getNomFichier)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
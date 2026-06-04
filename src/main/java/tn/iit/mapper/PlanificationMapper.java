package tn.iit.mapper;



import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import tn.iit.dto.PlanificationDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.PieceJointe;
import tn.iit.entity.Planification;
import tn.iit.entity.TransmissionCourrier;


@Component
public class PlanificationMapper {

	public PlanificationDTO toDTO(Planification p) {
	    PlanificationDTO dto = new PlanificationDTO();
	    dto.setId(p.getId());
	    dto.setDateEcheance(p.getDateEcheance());
	    dto.setStatut(p.getStatut() != null ? p.getStatut().name() : null);
	    dto.setResultat(p.getResultat() != null ? p.getResultat().name() : null);
	    dto.setCommentaireResultat(p.getCommentaireResultat());
	    dto.setCreatedAt(p.getCreatedAt());

	    if (p.getTransmission() != null) {
	        TransmissionCourrier t = p.getTransmission();
	        dto.setTransmissionId(t.getId());
	        dto.setTransmissionMessage(t.getMessage());

	        if (t.getDestinataire() != null) {
	            dto.setDestinataireId(t.getDestinataire().getId());
	            dto.setDestinataireNom(t.getDestinataire().getNom());
	        }

	        // ← courrier entrant + ses pièces jointes
	        if (t.getCourrierEntrant() != null) {
	            CourrierEntrant ce = t.getCourrierEntrant();
	            dto.setCourrierEntrantId(ce.getId());

	            if (ce.getPiecesJointes() != null) {
	                dto.setPiecesJointesIds(
	                    ce.getPiecesJointes().stream()
	                        .map(PieceJointe::getId)
	                        .collect(Collectors.toList())
	                );
	                dto.setPiecesJointesNoms(
	                    ce.getPiecesJointes().stream()
	                        .map(PieceJointe::getNomFichier)
	                        .collect(Collectors.toList())
	                );
	            }
	        }

	        // ← courrier sortant + ses pièces jointes
	        if (t.getCourrierSortant() != null) {
	            CourrierSortant cs = t.getCourrierSortant();
	            dto.setCourrierSortantId(cs.getId());

	            if (cs.getPiecesJointes() != null) {
	                dto.setPiecesJointesIds(
	                    cs.getPiecesJointes().stream()
	                        .map(PieceJointe::getId)
	                        .collect(Collectors.toList())
	                );
	                dto.setPiecesJointesNoms(
	                    cs.getPiecesJointes().stream()
	                        .map(PieceJointe::getNomFichier)
	                        .collect(Collectors.toList())
	                );
	            }
	        }
	    }

	    return dto;
	}


}
package tn.iit.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import tn.iit.dto.ArchiveEntrantDTO;
import tn.iit.dto.ArchiveSortantDTO;
import tn.iit.dto.CourrierEntrantDTO;
import tn.iit.dto.CourrierSortantDTO;
import tn.iit.dto.PieceJointeDTO;
import tn.iit.dto.TransmissionArchiveDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.EtatCourrier;
import tn.iit.entity.ModeExpedition;
import tn.iit.entity.ModeReception;
import tn.iit.entity.Planification;
import tn.iit.entity.Priorite;
import tn.iit.entity.StatutCourrier;
import tn.iit.entity.Tiers;

@Component
public class CourrierMapper {

    /*
     * ======================= COURRIER ENTRANT =======================
     */

	public CourrierEntrantDTO toDTO(CourrierEntrant courrier) {
	    if (courrier == null)
	        return null;

	    CourrierEntrantDTO dto = new CourrierEntrantDTO();
	    dto.setId(courrier.getId());
	    dto.setNumeroOrdre(courrier.getNumeroOrdre());
	    dto.setDateReception(courrier.getDateReception());
	    dto.setDateSaisie(courrier.getDateSaisie());
	    dto.setTypeDocument(courrier.getTypeDocument());
	    dto.setReference(courrier.getReference());
	    dto.setNature(courrier.getNature());
	    dto.setDateArchivage(courrier.getDateArchivage());

	    // Enums → String (protection null car champs nullable)
	    if (courrier.getModeReception() != null)
	        dto.setModeReception(courrier.getModeReception().name());
	    if (courrier.getStatut() != null)
	        dto.setStatut(courrier.getStatut().name());
	    if (courrier.getEtat() != null)
	        dto.setEtat(courrier.getEtat().name());
	    if (courrier.getPriorite() != null)
	        dto.setPriorite(courrier.getPriorite().name());

	    // Expéditeur externe (Tiers)
	    if (courrier.getExpediteur() != null) {
	        dto.setExpediteurId(courrier.getExpediteur().getId());
	        dto.setNomExpediteur(courrier.getExpediteur().getNom());
	    }

	    

	    return dto;
	}

	public CourrierEntrant toEntity(CourrierEntrantDTO dto) {
	    if (dto == null)
	        return null;

	    CourrierEntrant entity = new CourrierEntrant();
	    entity.setId(dto.getId());
	    entity.setDateReception(dto.getDateReception());
	    entity.setTypeDocument(dto.getTypeDocument());
	    entity.setReference(dto.getReference());
	    entity.setNature(dto.getNature());

	    // Enums → avec protection null
	    if (dto.getModeReception() != null)
	        entity.setModeReception(ModeReception.valueOf(dto.getModeReception()));
	    if (dto.getStatut() != null)
	        entity.setStatut(StatutCourrier.valueOf(dto.getStatut()));
	    if (dto.getEtat() != null)
	        entity.setEtat(EtatCourrier.valueOf(dto.getEtat()));
	    if (dto.getPriorite() != null)
	        entity.setPriorite(Priorite.valueOf(dto.getPriorite()));

	    // Expéditeur externe (Tiers) — stub suffisant, le service charge l'objet complet
	    if (dto.getExpediteurId() != null) {
	        Tiers tiers = new Tiers();
	        tiers.setId(dto.getExpediteurId());
	        entity.setExpediteur(tiers);
	    }

	    // dateSaisie, saisiPar, numeroOrdre → jamais mappés depuis le DTO
	    // ils sont gérés côté serveur (@PrePersist et Principal)

	    return entity;
	}
	public ArchiveEntrantDTO toArchiveEntrantDTO(CourrierEntrant entity) {
	    if (entity == null)
	        return null;
	    ArchiveEntrantDTO dto = new ArchiveEntrantDTO();
	    dto.setId(entity.getId());
	    dto.setNumeroOrdre(entity.getNumeroOrdre());
	    dto.setReference(entity.getReference());
	    dto.setDateReception(entity.getDateReception());
	    dto.setDateArchivage(entity.getDateArchivage());
	    dto.setNature(entity.getNature());

	    if (entity.getPriorite() != null)
	        dto.setPriorite(entity.getPriorite().name());

	    if (entity.getExpediteur() != null)
	        dto.setExpediteur(entity.getExpediteur().getNom());

	    if (entity.getTransmissions() != null) {
	        dto.setTransmissions(
	            entity.getTransmissions().stream()
	                .map(t -> {
	                    TransmissionArchiveDTO tDto = new TransmissionArchiveDTO();
	                    tDto.setId(t.getId());
	                    tDto.setMessage(t.getMessage());
	                    tDto.setDateEnvoi(t.getDateEnvoi());

	                    // protection null sur le destinataire
	                    if (t.getDestinataire() != null)
	                        tDto.setDestinataireNom(t.getDestinataire().getNom());

	                    // resultat + reponseEmploye viennent de la Planification liée
	                    if (t.getPlanification() != null) {
	                        Planification p = t.getPlanification();
	                        if (p.getResultat() != null)
	                            tDto.setResultat(p.getResultat().name());
	                        tDto.setReponseEmploye(p.getCommentaireResultat());
	                    }

	                    return tDto;
	                })
	                .collect(Collectors.toList())
	        );

	        dto.setPiecesJointes(
	            entity.getPiecesJointes().stream()
	                .map(p -> {
	                    PieceJointeDTO pDto = new PieceJointeDTO();
	                    pDto.setId(p.getId());
	                    pDto.setNomFichier(p.getNomFichier());
	                    return pDto;
	                })
	                .collect(Collectors.toList())
	        );
	    }
	    return dto;
	}

    /*
     * ======================= COURRIER SORTANT =======================
     */

	public CourrierSortantDTO toDTO(CourrierSortant courrier) {
	    if (courrier == null)
	        return null;

	    CourrierSortantDTO dto = new CourrierSortantDTO();
	    dto.setId(courrier.getId());
	    dto.setNumeroOrdre(courrier.getNumeroOrdre());
	    dto.setDateEmission(courrier.getDateEmission());
	    dto.setDateSaisie(courrier.getDateSaisie());
	    dto.setTypeDocument(courrier.getTypeDocument());
	    dto.setReference(courrier.getReference());
	    dto.setNature(courrier.getNature());
	    dto.setDateArchivage(courrier.getDateArchivage());
	    dto.setDateExpedition(courrier.getDateExpedition());

	    // Enums → String
	    if (courrier.getModeExpedition() != null)
	        dto.setModeExpedition(courrier.getModeExpedition().name());
	    if (courrier.getStatut() != null)
	        dto.setStatut(courrier.getStatut().name());
	    if (courrier.getEtat() != null)
	        dto.setEtat(courrier.getEtat().name());
	    if (courrier.getPriorite() != null)
	        dto.setPriorite(courrier.getPriorite().name());

	    // Destinataire externe (Tiers)
	    if (courrier.getDestinataire() != null) {
	        dto.setDestinataireId(courrier.getDestinataire().getId());
	        dto.setNomDestinataire(courrier.getDestinataire().getNom());
	       
	    }

	   

	    return dto;
	}

	public CourrierSortant toEntity(CourrierSortantDTO dto) {
	    if (dto == null)
	        return null;

	    CourrierSortant entity = new CourrierSortant();
	    entity.setId(dto.getId());
	    entity.setDateEmission(dto.getDateEmission());
	    entity.setTypeDocument(dto.getTypeDocument());
	    entity.setReference(dto.getReference());
	    entity.setNature(dto.getNature());

	    // Enums → avec protection null
	    if (dto.getModeExpedition() != null)
	        entity.setModeExpedition(ModeExpedition.valueOf(dto.getModeExpedition()));
	    if (dto.getStatut() != null)
	        entity.setStatut(StatutCourrier.valueOf(dto.getStatut()));
	    if (dto.getEtat() != null)
	        entity.setEtat(EtatCourrier.valueOf(dto.getEtat()));
	    if (dto.getPriorite() != null)
	        entity.setPriorite(Priorite.valueOf(dto.getPriorite()));

	    // Destinataire externe (Tiers) — stub, le service charge l'objet complet
	    if (dto.getDestinataireId() != null) {
	        Tiers tiers = new Tiers();
	        tiers.setId(dto.getDestinataireId());
	        entity.setDestinataire(tiers);
	    }

	    // dateSaisie, saisiPar, numeroOrdre → jamais mappés depuis le DTO
	    // gérés côté serveur (@PrePersist et Principal)

	    return entity;
	}
	public ArchiveSortantDTO toArchiveSortantDTO(CourrierSortant entity) {
	    if (entity == null)
	        return null;
	    ArchiveSortantDTO dto = new ArchiveSortantDTO();
	    dto.setId(entity.getId());
	    dto.setNumeroOrdre(entity.getNumeroOrdre());
	    dto.setReference(entity.getReference());
	    dto.setNature(entity.getNature());
	    dto.setDateEmission(entity.getDateEmission());
	    dto.setDateExpedition(entity.getDateExpedition());
	    dto.setDateArchivage(entity.getDateArchivage());

	    if (entity.getPriorite() != null)
	        dto.setPriorite(entity.getPriorite().name());
	    if (entity.getStatut() != null)
	        dto.setStatut(entity.getStatut().name());
	    if (entity.getEtat() != null)
	        dto.setEtat(entity.getEtat().name());

	    if (entity.getDestinataire() != null) {
	        dto.setDestinataire(entity.getDestinataire().getNom());
	    }

	    if (entity.getTransmissions() != null) {
	        dto.setTransmissions(
	            entity.getTransmissions().stream()
	                .map(t -> {
	                    TransmissionArchiveDTO tDto = new TransmissionArchiveDTO();
	                    tDto.setId(t.getId());
	                    tDto.setMessage(t.getMessage());
	                    tDto.setDateEnvoi(t.getDateEnvoi());

	                    if (t.getDestinataire() != null)
	                        tDto.setDestinataireNom(t.getDestinataire().getNom());

	                    if (t.getPlanification() != null) {
	                        Planification p = t.getPlanification();
	                        if (p.getResultat() != null)
	                            tDto.setResultat(p.getResultat().name());
	                        tDto.setReponseEmploye(p.getCommentaireResultat());
	                    }

	                    return tDto;
	                })
	                .collect(Collectors.toList())
	        );
	    }
	    if (entity.getPiecesJointes() != null) {
	        dto.setPiecesJointes(
	            entity.getPiecesJointes().stream()
	                .map(p -> {
	                    PieceJointeDTO pDto = new PieceJointeDTO();
	                    pDto.setId(p.getId());
	                    pDto.setNomFichier(p.getNomFichier());
	                    return pDto;
	                })
	                .collect(Collectors.toList())
	        );
	    }
	    return dto;
	}
  }
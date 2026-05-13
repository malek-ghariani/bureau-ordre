package tn.iit.mapper;

import org.springframework.stereotype.Component;

import tn.iit.dto.CourrierDTO;
import tn.iit.dto.CourrierEntrantDTO;
import tn.iit.dto.CourrierSortantDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.Departement;
import tn.iit.entity.Employe;
import tn.iit.entity.EtatCourrier;
import tn.iit.entity.ModeExpedition;
import tn.iit.entity.ModeReception;
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
		dto.setDateSaisie(courrier.getDateSaisie()); // ✅ lecture uniquement
		dto.setTypeDocument(courrier.getTypeDocument());
		dto.setReference(courrier.getReference());
		dto.setNature(courrier.getNature());
		dto.setExpediteur(courrier.getExpediteur());
		dto.setModeReception(courrier.getModeReception().name());
		dto.setStatut(courrier.getStatut().name());
		dto.setEtat(courrier.getEtat().name());
		dto.setPriorite(courrier.getPriorite().name());

		if (courrier.getTiers() != null) {
			dto.setTiersId(courrier.getTiers().getId());
			dto.setNomTiers(courrier.getTiers().getNom());
		}

		if (courrier.getDepartementDestinataire() != null) {
			dto.setDepartementDestinataireCode(courrier.getDepartementDestinataire().getCode());
			dto.setNomDepartementDestinataire(courrier.getDepartementDestinataire().getNom());
		}

		if (courrier.getEmploye() != null) {
			dto.setCreatedByMatricule(courrier.getEmploye().getMatricule());
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
		entity.setExpediteur(dto.getExpediteur());
		entity.setModeReception(ModeReception.valueOf(dto.getModeReception()));
		entity.setStatut(StatutCourrier.valueOf(dto.getStatut()));
		entity.setEtat(EtatCourrier.valueOf(dto.getEtat()));
		entity.setPriorite(Priorite.valueOf(dto.getPriorite()));

		if (dto.getDepartementDestinataireCode() != null) {
			Departement dep = new Departement();
			dep.setCode(dto.getDepartementDestinataireCode());
			entity.setDepartementDestinataire(dep);
		}

		if (dto.getTiersId() != null) {
			Tiers tiers = new Tiers();
			tiers.setId(dto.getTiersId());
			entity.setTiers(tiers);
		}

		if (dto.getCreatedByMatricule() != null) {
			Employe emp = new Employe();
			emp.setMatricule(dto.getCreatedByMatricule());
			entity.setEmploye(emp);
		}

		return entity;
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
		dto.setDateSaisie(courrier.getDateSaisie()); // ✅ lecture uniquement
		dto.setTypeDocument(courrier.getTypeDocument());
		dto.setReference(courrier.getReference());
		dto.setNature(courrier.getNature());
		dto.setDestinataire(courrier.getDestinataire());
		dto.setModeExpedition(courrier.getModeExpedition().name());
		dto.setStatut(courrier.getStatut().name());
		dto.setEtat(courrier.getEtat().name());
		dto.setPriorite(courrier.getPriorite().name());
		dto.setAdresseLivraison(courrier.getAdresseLivraison());
		dto.setEmailDestinataire(courrier.getEmailDestinataire());

		if (courrier.getTiers() != null) {
			dto.setTiersId(courrier.getTiers().getId());
			dto.setNomTiers(courrier.getTiers().getNom());
		}

		if (courrier.getDepartementEmetteur() != null) {
			dto.setDepartementEmetteurCode(courrier.getDepartementEmetteur().getCode());
			dto.setNomDepartementEmetteur(courrier.getDepartementEmetteur().getNom());
		}

		if (courrier.getEmploye() != null) {
			dto.setCreatedByMatricule(courrier.getEmploye().getMatricule());
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
		entity.setDestinataire(dto.getDestinataire());
		entity.setModeExpedition(ModeExpedition.valueOf(dto.getModeExpedition()));
		entity.setStatut(StatutCourrier.valueOf(dto.getStatut()));
		entity.setEtat(EtatCourrier.valueOf(dto.getEtat()));
		entity.setPriorite(Priorite.valueOf(dto.getPriorite()));
		entity.setAdresseLivraison(dto.getAdresseLivraison());
		entity.setEmailDestinataire(dto.getEmailDestinataire());

		if (dto.getTiersId() != null) {
			Tiers tiers = new Tiers();
			tiers.setId(dto.getTiersId());
			entity.setTiers(tiers);
		}

		if (dto.getDepartementEmetteurCode() != null) {
			Departement dep = new Departement();
			dep.setCode(dto.getDepartementEmetteurCode());
			entity.setDepartementEmetteur(dep);
		}

		return entity;
	}

	/*
	 * ======================= MAPPER POUR LISTE UNIQUE =======================
	 */

	public CourrierDTO toUnifiedDTO(CourrierEntrant c, String type) {
		if (c == null)
			return null;

		CourrierDTO dto = new CourrierDTO();
		dto.setId(c.getId());
		dto.setNumeroOrdre(c.getNumeroOrdre());
		dto.setDate(c.getDateReception());
		dto.setType(type); // "ENTRANT"
		dto.setReference(c.getReference());
		dto.setNature(c.getNature());
		dto.setExpediteur(c.getExpediteur());
		dto.setStatut(c.getStatut().name());
		dto.setEtat(c.getEtat().name());

		return dto;
	}

	public CourrierDTO toUnifiedDTO(CourrierSortant c, String type) {
		if (c == null)
			return null;

		CourrierDTO dto = new CourrierDTO();
		dto.setId(c.getId());
		dto.setNumeroOrdre(c.getNumeroOrdre());
		dto.setDate(c.getDateEmission());
		dto.setType(type); // "SORTANT"
		dto.setReference(c.getReference());
		dto.setNature(c.getNature());
		dto.setStatut(c.getStatut().name());
		dto.setEtat(c.getEtat().name());

		return dto;
	}

}

package tn.iit.service;

import tn.iit.entity.CourrierSortant;
import tn.iit.entity.Employe;
import tn.iit.entity.StatutCourrier;
import tn.iit.repository.CourrierSortantRepository;
import tn.iit.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourrierSortantService {

	private final CourrierSortantRepository courrierSortantRepository;
	private final EmployeRepository employeRepository;

	public List<CourrierSortant> findAll() {
		return courrierSortantRepository.findAll();
	}

	public Optional<CourrierSortant> findById(Long id) {
		return courrierSortantRepository.findById(id);
	}

	public Optional<CourrierSortant> findByNumeroOrdre(String numeroOrdre) {
		return courrierSortantRepository.findByNumeroOrdre(numeroOrdre);
	}

	public CourrierSortant save(CourrierSortant courrierSortant) {
		return courrierSortantRepository.save(courrierSortant);
	}

	public void deleteById(Long id) {
		courrierSortantRepository.deleteById(id);
	}

	public List<CourrierSortant> findByDepartementEmetteur(String codeDepartement) {
		return courrierSortantRepository.findByDepartementEmetteurCode(codeDepartement);
	}

	public List<CourrierSortant> findByStatut(String statut) {
		return courrierSortantRepository.findByStatut(statut);
	}

	public List<CourrierSortant> findByModeExpedition(String modeExpedition) {
		return courrierSortantRepository.findByModeExpedition(modeExpedition);
	}

	public List<CourrierSortant> findByDestinataire(String destinataire) {
		return courrierSortantRepository.findByDestinataireContainingIgnoreCase(destinataire);
	}

	public List<CourrierSortant> findByDateEmissionBetween(LocalDate startDate, LocalDate endDate) {
		return courrierSortantRepository.findByDateEmissionBetween(startDate, endDate);
	}

	public List<CourrierSortant> findByTiers(Long tiersId) {
		return courrierSortantRepository.findByTiersId(tiersId);
	}

	public List<CourrierSortant> findByReference(String reference) {
		return courrierSortantRepository.findByReferenceContaining(reference);
	}

	public List<CourrierSortant> findByNature(String nature) {
		return courrierSortantRepository.findByNatureContaining(nature);
	}

	public Long countCourriersByDate(LocalDate date) {
		return courrierSortantRepository.countByDateEmission(date);
	}

	public List<CourrierSortant> findByEmploye(Employe employe) {
		return courrierSortantRepository.findByEmploye(employe);
	}

	public CourrierSortant assignerEmploye(Long courrierId, Long employeId) {
		CourrierSortant courrier = courrierSortantRepository.findById(courrierId)
				.orElseThrow(() -> new RuntimeException("Courrier introuvable"));

		Employe employe = employeRepository.findById(employeId)
				.orElseThrow(() -> new RuntimeException("Employé introuvable"));

		courrier.setEmploye(employe);
		courrier.setStatut(StatutCourrier.EN_COURS);
		return courrierSortantRepository.save(courrier);
	}

}
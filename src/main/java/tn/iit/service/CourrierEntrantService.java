package tn.iit.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.Employe;
import tn.iit.entity.StatutCourrier;
import tn.iit.repository.CourrierEntrantRepository;
import tn.iit.repository.EmployeRepository;

@Service
@RequiredArgsConstructor
public class CourrierEntrantService {
    
    private final CourrierEntrantRepository courrierEntrantRepository;
    private final EmployeRepository employeRepository;
    
    public List<CourrierEntrant> findAll() {
        return courrierEntrantRepository.findAll();
    }
    
    public Optional<CourrierEntrant> findById(Long id) {
        return courrierEntrantRepository.findById(id);
    }
    
    public Optional<CourrierEntrant> findByNumeroOrdre(String numeroOrdre) {
        return courrierEntrantRepository.findByNumeroOrdre(numeroOrdre);
    }
    
    public CourrierEntrant save(CourrierEntrant courrierEntrant) {
        return courrierEntrantRepository.save(courrierEntrant);
    }
    
    public void deleteById(Long id) {
        courrierEntrantRepository.deleteById(id);
    }
    
    public List<CourrierEntrant> findByDepartementDestinataire(String codeDepartement) {
        return courrierEntrantRepository.findByDepartementDestinataireCode(codeDepartement);
    }
    
    public List<CourrierEntrant> findByStatut(String statut) {
        return courrierEntrantRepository.findByStatut(statut);
    }
    
    public List<CourrierEntrant> findByModeReception(String modeReception) {
        return courrierEntrantRepository.findByModeReception(modeReception);
    }
    
    public List<CourrierEntrant> findByPriorite(String priorite) {
        return courrierEntrantRepository.findByPriorite(priorite);
    }
    
    public List<CourrierEntrant> findByExpediteur(String expediteur) {
        return courrierEntrantRepository.findByExpediteurContainingIgnoreCase(expediteur);
    }
    
    public List<CourrierEntrant> findByDateReceptionBetween(LocalDate startDate, LocalDate endDate) {
        return courrierEntrantRepository.findByDateReceptionBetween(startDate, endDate);
    }
    
    public List<CourrierEntrant> findByTiers(Long tiersId) {
        return courrierEntrantRepository.findByTiersId(tiersId);
    }
    
    public List<CourrierEntrant> findByReference(String reference) {
        return courrierEntrantRepository.findByReferenceContaining(reference);
    }
    
    public List<CourrierEntrant> findByNature(String nature) {
        return courrierEntrantRepository.findByNatureContaining(nature);
    }
    
    public Long countCourriersByDate(LocalDate date) {
        return courrierEntrantRepository.countByDateReception(date);
    }
    public List<CourrierEntrant> findByEmploye(Employe employe) {
        return courrierEntrantRepository.findByEmploye(employe);
    }
    public CourrierEntrant assignerEmploye(Long courrierId, Long employeId) {

        CourrierEntrant courrier = courrierEntrantRepository.findById(courrierId)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        // Affectation
        courrier.setEmploye(employe);

        // Statut → utiliser l'enum, pas une string brute
        courrier.setStatut(StatutCourrier.EN_COURS);

        

        return courrierEntrantRepository.save(courrier);
    }

}
package tn.iit.repository;

import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.Employe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourrierEntrantRepository extends JpaRepository<CourrierEntrant, Long> {
    Optional<CourrierEntrant> findByNumeroOrdre(String numeroOrdre);
    List<CourrierEntrant> findByDepartementDestinataireCode(String codeDepartement);
    List<CourrierEntrant> findByStatut(String statut);
    List<CourrierEntrant> findByModeReception(String modeReception);
    List<CourrierEntrant> findByPriorite(String priorite);
    List<CourrierEntrant> findByExpediteurContainingIgnoreCase(String expediteur);
    List<CourrierEntrant> findByDateReceptionBetween(LocalDate startDate, LocalDate endDate);
    List<CourrierEntrant> findByEmployeMatricule(String matricule);
    List<CourrierEntrant> findByEmploye(Employe employe);
    @Query("SELECT COUNT(c) FROM CourrierEntrant c WHERE c.dateReception = :date")
    Long countByDateReception(LocalDate date);
    
    @Query("SELECT c FROM CourrierEntrant c WHERE c.reference LIKE %:reference%")
    List<CourrierEntrant> findByReferenceContaining(String reference);
    
    List<CourrierEntrant> findByTiersId(Long tiersId);
    
    @Query("SELECT c FROM CourrierEntrant c WHERE c.nature LIKE %:nature%")
    List<CourrierEntrant> findByNatureContaining(String nature);
}
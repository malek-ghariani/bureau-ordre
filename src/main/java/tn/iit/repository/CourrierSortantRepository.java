package tn.iit.repository;

import tn.iit.entity.CourrierSortant;
import tn.iit.entity.Employe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourrierSortantRepository extends JpaRepository<CourrierSortant, Long> {
    Optional<CourrierSortant> findByNumeroOrdre(String numeroOrdre);
    List<CourrierSortant> findByDepartementEmetteurCode(String codeDepartement);
    List<CourrierSortant> findByStatut(String statut);
    List<CourrierSortant> findByModeExpedition(String modeExpedition);
    List<CourrierSortant> findByDestinataireContainingIgnoreCase(String destinataire);
    List<CourrierSortant> findByDateEmissionBetween(LocalDate startDate, LocalDate endDate);
    List<CourrierSortant> findByEmployeMatricule(String matricule);
    
    @Query("SELECT COUNT(c) FROM CourrierSortant c WHERE c.dateEmission = :date")
    Long countByDateEmission(LocalDate date);
    
    @Query("SELECT c FROM CourrierSortant c WHERE c.reference LIKE %:reference%")
    List<CourrierSortant> findByReferenceContaining(String reference);
    
    List<CourrierSortant> findByTiersId(Long tiersId);
    
    @Query("SELECT c FROM CourrierSortant c WHERE c.nature LIKE %:nature%")
    List<CourrierSortant> findByNatureContaining(String nature);
	List<CourrierSortant> findByEmploye(Employe employe);
}
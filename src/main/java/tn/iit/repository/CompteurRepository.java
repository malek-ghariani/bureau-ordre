package tn.iit.repository;

import tn.iit.entity.Compteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompteurRepository extends JpaRepository<Compteur, Long> {
    Optional<Compteur> findByPrefixe(String prefixe);
    boolean existsByPrefixe(String prefixe);
    
    @Modifying
    @Query("UPDATE Compteur c SET c.valeurActuelle = c.valeurActuelle + 1 WHERE c.prefixe = :prefixe")
    void incrementValeurActuelle(String prefixe);
    
    @Modifying
    @Query("UPDATE Compteur c SET c.anneeCourante = :annee, c.valeurActuelle = 1 WHERE c.prefixe = :prefixe")
    void resetCompteurForNewYear(String prefixe, Integer annee);
}
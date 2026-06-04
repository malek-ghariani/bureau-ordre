package tn.iit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tn.iit.entity.Compteur;



@Repository
public interface CompteurRepository extends JpaRepository<Compteur, String> { // String car prefixe est l'ID

    @Modifying
    @Transactional 
    @Query("UPDATE Compteur c SET c.valeurActuelle = c.valeurActuelle + 1 WHERE c.prefixe = :prefixe")
    void incrementValeurActuelle(String prefixe);
}
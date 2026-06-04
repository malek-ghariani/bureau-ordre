package tn.iit.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.EtatCourrier;


@Repository
public interface CourrierEntrantRepository extends JpaRepository<CourrierEntrant, Long> {
   
	List<CourrierEntrant> findByEtat(EtatCourrier etat);
}
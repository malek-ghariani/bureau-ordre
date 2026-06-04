package tn.iit.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import tn.iit.entity.CourrierSortant;
import tn.iit.entity.EtatCourrier;


@Repository
public interface CourrierSortantRepository extends JpaRepository<CourrierSortant, Long> {
   	
	List<CourrierSortant> findByEtat(EtatCourrier etat);
}
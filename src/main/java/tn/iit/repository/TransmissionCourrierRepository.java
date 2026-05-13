package tn.iit.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import tn.iit.entity.TransmissionCourrier;

public interface TransmissionCourrierRepository extends JpaRepository<TransmissionCourrier, Long> {

	List<TransmissionCourrier> findByDestinataireId(Long id);

	List<TransmissionCourrier> findByExpediteurId(Long id);
	
	List<TransmissionCourrier> findByCourrierEntrant_Id(Long id);
	List<TransmissionCourrier> findByCourrierSortant_Id(Long id);

	 
}

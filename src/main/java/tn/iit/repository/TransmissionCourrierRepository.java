package tn.iit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.iit.entity.TransmissionCourrier;
@Repository
public interface TransmissionCourrierRepository extends JpaRepository<TransmissionCourrier, Long> {

	List<TransmissionCourrier> findByDestinataire_Id(Long id);
	List<TransmissionCourrier> findByCourrierEntrant_Id(Long id);
	List<TransmissionCourrier> findByCourrierSortant_Id(Long id);

	 
}

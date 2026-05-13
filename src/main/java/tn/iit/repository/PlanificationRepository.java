package tn.iit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.iit.entity.Planification;

public interface PlanificationRepository extends JpaRepository<Planification, Long> {

   
    List<Planification> findByDestinataireId(Long id);
   
    

}

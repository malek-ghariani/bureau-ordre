package tn.iit.repository;

import tn.iit.entity.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, String> {
    Optional<Departement> findByCode(String code);
    List<Departement> findByNomContainingIgnoreCase(String nom);
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
    List<Departement> findByChefDepartement(String chefDepartement);
}
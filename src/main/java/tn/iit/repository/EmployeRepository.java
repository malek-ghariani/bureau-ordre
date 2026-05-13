package tn.iit.repository;

import tn.iit.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Optional<Employe> findByEmail(String email);
    Optional<Employe> findByMatricule(String matricule);
    boolean existsByEmail(String email);
    boolean existsByMatricule(String matricule);
    List<Employe> findByDepartementCode(String codeDepartement);
    List<Employe> findByRole(String role);
    List<Employe> findByEnabledTrue();
    Optional<Employe> findByEmailAndEnabledTrue(String email);
    List<Employe> findByNomContainingIgnoreCase(String nom);
    

}
package tn.iit.repository;

import tn.iit.entity.Tiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TiersRepository extends JpaRepository<Tiers, Long> {
    List<Tiers> findByType(String type);
    List<Tiers> findByNomContainingIgnoreCase(String nom);
    Optional<Tiers> findByEmail(String email);
    //Optional<Tiers> findByIce(String ice);
    List<Tiers> findByNomContactContainingIgnoreCase(String nomContact);
    boolean existsByEmail(String email);
    //boolean existsByIce(String ice);
    List<Tiers> findByNomStartingWithIgnoreCase(String nom);
}
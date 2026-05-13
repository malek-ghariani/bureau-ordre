package tn.iit.repository;

import tn.iit.entity.PieceJointe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceJointeRepository extends JpaRepository<PieceJointe, Long> {
    List<PieceJointe> findByCourrierEntrantId(Long courrierEntrantId);
    List<PieceJointe> findByCourrierSortantId(Long courrierSortantId);
    List<PieceJointe> findByUploadedByMatricule(String matricule);
    void deleteByCourrierEntrantId(Long courrierEntrantId);
    void deleteByCourrierSortantId(Long courrierSortantId);
    
    @Query("SELECT p FROM PieceJointe p WHERE p.nomFichier LIKE %:nomFichier%")
    List<PieceJointe> findByNomFichierContaining(String nomFichier);
}
package tn.iit.service;

import tn.iit.entity.PieceJointe;
import tn.iit.repository.PieceJointeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PieceJointeService {
    
    private final PieceJointeRepository pieceJointeRepository;
    
    public List<PieceJointe> findAll() {
        return pieceJointeRepository.findAll();
    }
    
    public Optional<PieceJointe> findById(Long id) {
        return pieceJointeRepository.findById(id);
    }
    
    public PieceJointe save(PieceJointe pieceJointe) {
        return pieceJointeRepository.save(pieceJointe);
    }
    
    public void deleteById(Long id) {
        pieceJointeRepository.deleteById(id);
    }
    
    public List<PieceJointe> findByCourrierEntrant(Long courrierEntrantId) {
        return pieceJointeRepository.findByCourrierEntrantId(courrierEntrantId);
    }
    
    public List<PieceJointe> findByCourrierSortant(Long courrierSortantId) {
        return pieceJointeRepository.findByCourrierSortantId(courrierSortantId);
    }
    
    public List<PieceJointe> findByUploadedBy(String matricule) {
        return pieceJointeRepository.findByUploadedByMatricule(matricule);
    }
    
    public void deleteByCourrierEntrant(Long courrierEntrantId) {
        pieceJointeRepository.deleteByCourrierEntrantId(courrierEntrantId);
    }
    
    public void deleteByCourrierSortant(Long courrierSortantId) {
        pieceJointeRepository.deleteByCourrierSortantId(courrierSortantId);
    }
    
    public List<PieceJointe> findByNomFichier(String nomFichier) {
        return pieceJointeRepository.findByNomFichierContaining(nomFichier);
    }
}
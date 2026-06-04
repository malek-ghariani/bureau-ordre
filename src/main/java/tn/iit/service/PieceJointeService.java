package tn.iit.service;

import java.io.IOException;          
import java.nio.file.Files;
import java.nio.file.Path;           
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.PieceJointe;
import tn.iit.repository.PieceJointeRepository;

@Service
@RequiredArgsConstructor
public class PieceJointeService {

    private final PieceJointeRepository pieceJointeRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;  // ← pas final, donc @Value fonctionne

    private Path getStorageLocation() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public PieceJointe uploadEntrant(MultipartFile file, String description, 
                               CourrierEntrant courrier) throws IOException {
        Path storageLocation = getStorageLocation();
        Files.createDirectories(storageLocation);

        String storedFileName = UUID.randomUUID() + getExtension(file.getOriginalFilename());
        Path targetLocation = storageLocation.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        PieceJointe pj = new PieceJointe();
        pj.setNomFichier(file.getOriginalFilename());
        pj.setCheminStockage(targetLocation.toString());
        pj.setDescription(description);
        pj.setTaille(file.getSize());
        pj.setCourrierEntrant(courrier);

        return pieceJointeRepository.save(pj);
    }

    public PieceJointe uploadSortant(MultipartFile file, String description,
                                      CourrierSortant courrier) throws IOException {
        Path storageLocation = getStorageLocation();
        Files.createDirectories(storageLocation);

        String storedFileName = UUID.randomUUID() + getExtension(file.getOriginalFilename());
        Path targetLocation = storageLocation.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        PieceJointe pj = new PieceJointe();
        pj.setNomFichier(file.getOriginalFilename());
        pj.setCheminStockage(targetLocation.toString());
        pj.setDescription(description);
        pj.setTaille(file.getSize());
        pj.setCourrierSortant(courrier);

        return pieceJointeRepository.save(pj);
    }

    private String getExtension(String name) {
        if (name == null || !name.contains(".")) return "";
        return name.substring(name.lastIndexOf("."));
    }

  
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
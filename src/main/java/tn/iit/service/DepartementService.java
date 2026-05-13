package tn.iit.service;

import tn.iit.entity.Departement;
import tn.iit.repository.DepartementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartementService {
    
    private final DepartementRepository departementRepository;
    
    public List<Departement> findAll() {
        return departementRepository.findAll();
    }
    
    public Optional<Departement> findById(String code) {
        return departementRepository.findById(code);
    }
    
    public Departement save(Departement departement) {
        return departementRepository.save(departement);
    }
    
    public void deleteById(String code) {
        departementRepository.deleteById(code);
    }
    
    public boolean existsByCode(String code) {
        return departementRepository.existsByCode(code);
    }
    
    public List<Departement> findByNomContaining(String nom) {
        return departementRepository.findByNomContainingIgnoreCase(nom);
    }
    
    public List<Departement> findByChefDepartement(String chefDepartement) {
        return departementRepository.findByChefDepartement(chefDepartement);
    }
}
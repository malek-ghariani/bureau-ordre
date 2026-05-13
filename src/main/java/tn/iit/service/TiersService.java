package tn.iit.service;

import tn.iit.entity.Tiers;
import tn.iit.repository.TiersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TiersService {
    
    private final TiersRepository tiersRepository;
    
    public List<Tiers> findAll() {
        return tiersRepository.findAll();
    }
    
    public Optional<Tiers> findById(Long id) {
        return tiersRepository.findById(id);
    }
    
    public Tiers save(Tiers tiers) {
        return tiersRepository.save(tiers);
    }
    
    public void deleteById(Long id) {
        tiersRepository.deleteById(id);
    }
    
    public List<Tiers> findByType(String type) {
        return tiersRepository.findByType(type);
    }
    
    public List<Tiers> findByNomContaining(String nom) {
        return tiersRepository.findByNomContainingIgnoreCase(nom);
    }
    
    public Optional<Tiers> findByEmail(String email) {
        return tiersRepository.findByEmail(email);
    }
    
  
    
    public boolean existsByEmail(String email) {
        return tiersRepository.existsByEmail(email);
    }
    
    
    
    public List<Tiers> searchTiers(String keyword) {
        return tiersRepository.findByNomContainingIgnoreCase(keyword);
    }
}
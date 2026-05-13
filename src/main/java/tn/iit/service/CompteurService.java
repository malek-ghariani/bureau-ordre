package tn.iit.service;

import tn.iit.entity.Compteur;
import tn.iit.repository.CompteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompteurService {
    
    private final CompteurRepository compteurRepository;
    
    public Optional<Compteur> findByPrefixe(String prefixe) {
        return compteurRepository.findByPrefixe(prefixe);
    }
    
    public Compteur save(Compteur compteur) {
        return compteurRepository.save(compteur);
    }
    
    @Transactional
    public synchronized String genererNumero(String prefixe) {
        Optional<Compteur> compteurOpt = compteurRepository.findByPrefixe(prefixe);
        Compteur compteur;
        
        if (compteurOpt.isPresent()) {
            compteur = compteurOpt.get();
            compteurRepository.incrementValeurActuelle(prefixe);
        } else {
            compteur = new Compteur();
            compteur.setPrefixe(prefixe);
            compteur.setValeurActuelle(1);
            compteur.setDescription("Compteur pour " + prefixe);
            compteur = compteurRepository.save(compteur);
        }
        
        return String.format("%s-%06d", prefixe, compteur.getValeurActuelle());
    }
    
    public void initialiserCompteur(String prefixe, Integer valeurInitiale) {
        Compteur compteur = new Compteur();
        compteur.setPrefixe(prefixe);
        compteur.setValeurActuelle(valeurInitiale);
        compteur.setDescription("Compteur initialisé pour " + prefixe);
        compteurRepository.save(compteur);
    }
    
    @Transactional
    public void resetCompteurForNewYear(String prefixe, Integer nouvelleAnnee) {
        compteurRepository.resetCompteurForNewYear(prefixe, nouvelleAnnee);
    }
}
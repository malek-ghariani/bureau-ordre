package tn.iit.service;

import tn.iit.entity.Compteur;
import tn.iit.repository.CompteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class CompteurService {

    private final CompteurRepository compteurRepository;

    @Transactional
    public synchronized String genererNumero(String prefixe) {
        compteurRepository.incrementValeurActuelle(prefixe);  // incrémente d'abord
        Compteur compteur = compteurRepository.findById(prefixe)
            .orElseThrow(() -> new RuntimeException("Compteur introuvable"));
        return String.format("%s-%d-%06d", prefixe,compteur.getAnneeCourante(), compteur.getValeurActuelle());
    }
}
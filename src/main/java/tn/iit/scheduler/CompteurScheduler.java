package tn.iit.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.iit.repository.CompteurRepository;

@Component
@RequiredArgsConstructor
public class CompteurScheduler {

    private final CompteurRepository compteurRepository;

    @Scheduled(cron = "0 0 0 1 1 *") // chaque 1er janvier à minuit
    @Transactional
    public void resetPourNouvelleAnnee() {
        int nouvelleAnnee = LocalDate.now().getYear();
        compteurRepository.findAll().forEach(compteur -> {
            compteur.setValeurActuelle(0);
            compteur.setAnneeCourante(nouvelleAnnee);
            compteurRepository.save(compteur);
        });
    }
}
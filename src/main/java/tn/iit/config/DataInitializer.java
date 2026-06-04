package tn.iit.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tn.iit.entity.Compteur;
import tn.iit.repository.CompteurRepository;


@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

 private final CompteurRepository compteurRepository;

 @Override
 public void run(ApplicationArguments args) {
     creerSiExistePas("CE");
     creerSiExistePas("CS");
 }

 private void creerSiExistePas(String prefixe) {
     if (!compteurRepository.existsById(prefixe)) {
         Compteur c = new Compteur();
         c.setPrefixe(prefixe);
         compteurRepository.save(c);
     }
 }
}
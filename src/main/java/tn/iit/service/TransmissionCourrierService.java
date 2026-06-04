package tn.iit.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.TransmissionRequest;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.Employe;
import tn.iit.entity.StatutCourrier;
import tn.iit.entity.TransmissionCourrier;
import tn.iit.repository.CourrierEntrantRepository;
import tn.iit.repository.CourrierSortantRepository;
import tn.iit.repository.EmployeRepository;
import tn.iit.repository.TransmissionCourrierRepository;
@Service
@RequiredArgsConstructor
public class TransmissionCourrierService {

    private final TransmissionCourrierRepository repo;
    private final EmployeRepository employeRepo;
    private final CourrierEntrantRepository ceRepo;
    private final CourrierSortantRepository csRepo;
    private final PlanificationService planificationService;
  

    // =========================
    // ENVOYER COURRIER
    // =========================
    public TransmissionCourrier envoyerCourrier(TransmissionRequest request) {

        if (request.getType() == null) {
            throw new RuntimeException("Le type de courrier est obligatoire");
        }

        TransmissionCourrier t = new TransmissionCourrier();

        // -------- TYPE COURRIER --------
        switch (request.getType().toUpperCase()) {

            case "ENTRANT" -> {
                CourrierEntrant ce = ceRepo.findById(request.getCourrierId())
                        .orElseThrow(() -> new RuntimeException("Courrier entrant introuvable"));
                t.setCourrierEntrant(ce);
            }

            case "SORTANT" -> {
                CourrierSortant cs = csRepo.findById(request.getCourrierId())
                        .orElseThrow(() -> new RuntimeException("Courrier sortant introuvable"));
                t.setCourrierSortant(cs);
            }

            default -> throw new RuntimeException("Type de courrier invalide : " + request.getType());
        }

     
        Employe destinataire = employeRepo.findById(request.getDestinataireId())
                .orElseThrow(() -> new RuntimeException("Destinataire introuvable"));

        t.setDestinataire(destinataire);

        // -------- CONTENU --------
        t.setMessage(request.getMessage());
       

        // dateEnvoi gérée automatiquement par @PrePersist

        TransmissionCourrier saved = repo.save(t);
     // après saved = repo.save(t);
        if (t.getCourrierEntrant() != null) {
            CourrierEntrant ce = t.getCourrierEntrant();
            ce.setStatut(StatutCourrier.EN_COURS);
            ceRepo.save(ce);
        }
        if (t.getCourrierSortant() != null) {
            CourrierSortant cs = t.getCourrierSortant();
            cs.setStatut(StatutCourrier.EN_COURS);
            csRepo.save(cs);
        }

        // ✔ créer planification automatiquement
        planificationService.creerDepuisTransmission(
                saved.getId(),
                request.getDateEcheance()
        );

        return saved;
    }
    



    // =========================
    // REÇUS / ENVOYÉS
    // =========================
    public List<TransmissionCourrier> getRecus(Long destinataireId) {
        return repo.findByDestinataire_Id(destinataireId);
    }

   

    // =========================
    // MARQUER COMME LU
    // =========================
    public TransmissionCourrier marquerCommeLu(Long id) {

        TransmissionCourrier t = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transmission introuvable"));

       
        t.setDateLecture(LocalDateTime.now());

        return repo.save(t);
    }

   

    // ==========================
    // GET PAR COURRIER ENTRANT
    // ==========================
    public List<TransmissionCourrier> getByCourrierEntrant(Long courrierId) {

        List<TransmissionCourrier> list = repo.findByCourrierEntrant_Id(courrierId);

        if (list.isEmpty()) {
            throw new RuntimeException("Aucune transmission trouvée pour ce courrier entrant");
        }

        return list;
    }

    // ==========================
    // GET PAR COURRIER SORTANT
    // ==========================
    public List<TransmissionCourrier> getByCourrierSortant(Long courrierId) {

        List<TransmissionCourrier> list = repo.findByCourrierSortant_Id(courrierId);

        if (list.isEmpty()) {
            throw new RuntimeException("Aucune transmission trouvée pour ce courrier sortant");
        }

        return list;
    }
}
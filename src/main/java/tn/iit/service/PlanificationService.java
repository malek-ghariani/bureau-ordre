package tn.iit.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.PlanificationDTO;
import tn.iit.dto.ResponsePlanificationDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;

import tn.iit.entity.Planification;
import tn.iit.entity.ResultatTraitement;
import tn.iit.entity.StatutCourrier;
import tn.iit.entity.StatutPlanification;
import tn.iit.entity.TransmissionCourrier;
import tn.iit.mapper.PlanificationMapper;
import tn.iit.repository.CourrierEntrantRepository;
import tn.iit.repository.CourrierSortantRepository;

import tn.iit.repository.PlanificationRepository;
import tn.iit.repository.TransmissionCourrierRepository;


@Service
@RequiredArgsConstructor
public class PlanificationService {

    private final PlanificationRepository planificationRepository;
    private final CourrierEntrantRepository courrierEntrantRepository;
    private final CourrierSortantRepository courrierSortantRepository;
    private final PlanificationMapper planificationMapper;
    private final TransmissionCourrierRepository transmissionRepository;

    // =========================
    //  CRÉER DEPUIS TRANSMISSION
    //  Seul point de création
    // =========================
    @Transactional
    public void creerDepuisTransmission(Long transmissionId, LocalDateTime dateEcheance) {

        TransmissionCourrier transmission = transmissionRepository.findById(transmissionId)
                .orElseThrow(() -> new RuntimeException("Transmission introuvable"));

        // Courrier entrant
        if (transmission.getCourrierEntrant() != null) {
            CourrierEntrant ce = transmission.getCourrierEntrant();
            ce.setStatut(StatutCourrier.EN_COURS);
            courrierEntrantRepository.save(ce);
        }

        // Courrier sortant
        if (transmission.getCourrierSortant() != null) {
            CourrierSortant cs = transmission.getCourrierSortant();
            cs.setStatut(StatutCourrier.EN_COURS);
            courrierSortantRepository.save(cs);
        }

        Planification p = new Planification();
        p.setDateEcheance(dateEcheance);
        p.setStatut(StatutPlanification.EN_ATTENTE);
        
        p.setTransmission(transmission);

        planificationRepository.save(p);
    }
    @Transactional
    public PlanificationDTO update(Long id, PlanificationDTO dto) {

        Planification p = planificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));

        // mise à jour des champs modifiables
        if (dto.getDateEcheance() != null) {
            p.setDateEcheance(dto.getDateEcheance());
        }

        if (dto.getCommentaireResultat() != null) {
            p.setCommentaireResultat(dto.getCommentaireResultat());
        }

        if (dto.getResultat() != null) {
            p.setResultat(ResultatTraitement.valueOf(dto.getResultat()));
        }

        if (dto.getStatut() != null) {
            p.setStatut(StatutPlanification.valueOf(dto.getStatut()));
        }

        Planification saved = planificationRepository.save(p);

        return planificationMapper.toDTO(saved);
    }

    // =========================
    //  RÉPONDRE
    //  Action de l'employé
    // =========================
    @Transactional
    public PlanificationDTO repondre(Long id, ResponsePlanificationDTO dto) {
        Planification p = planificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));

        p.setCommentaireResultat(dto.getCommentaireResultat());
        p.setResultat(dto.getResultat());
        p.setStatut(StatutPlanification.TRAITE);

        return planificationMapper.toDTO(planificationRepository.save(p));
    }

   

    // =========================
    //  GET BY ID
    // =========================
    public PlanificationDTO getById(Long id) {
        return planificationRepository.findById(id)
                .map(planificationMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));
    }



    // =========================
    //  VUE EMPLOYÉ
    //  Ses propres planifications
    // =========================
    public List<PlanificationDTO> findByDestinataire(Long employeId) {
        return planificationRepository.findByTransmission_Destinataire_Id(employeId)
                .stream()
                .map(planificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // =========================
    //  DELETE
    // =========================
    @Transactional
    public void delete(Long id) {
        Planification p = planificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));
        planificationRepository.delete(p);
    }
    public List<PlanificationDTO> getAll() {
        return planificationRepository.findAll()
                .stream()
                .map(planificationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
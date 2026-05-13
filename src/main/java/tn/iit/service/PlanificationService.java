package tn.iit.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.PlanificationDTO;
import tn.iit.dto.ResponsePlanificationDTO;
import tn.iit.entity.PieceJointe;
import tn.iit.entity.Planification;
import tn.iit.entity.ResultatTraitement;
import tn.iit.entity.StatutPlanification;

import tn.iit.entity.TransmissionCourrier;
import tn.iit.entity.TypeSourcePlanification;
import tn.iit.mapper.PlanificationMapper;
import tn.iit.repository.CourrierEntrantRepository;
import tn.iit.repository.CourrierSortantRepository;
import tn.iit.repository.EmployeRepository;
import tn.iit.repository.PieceJointeRepository;
import tn.iit.repository.PlanificationRepository;
import tn.iit.repository.TransmissionCourrierRepository;

@Service
@RequiredArgsConstructor
public class PlanificationService {

    private final PlanificationRepository planificationRepository;
    private final EmployeRepository employeRepository;
    private final CourrierEntrantRepository courrierEntrantRepository;
    private final CourrierSortantRepository courrierSortantRepository;
    private final PieceJointeRepository pieceJointeRepository;
    private final TransmissionCourrierRepository transmissionRepository;


    // =========================
    //       CREATE
    // =========================
    public Planification create(PlanificationDTO dto) {

        Planification p = new Planification();

        p.setMessage(dto.getMessage());
        p.setDateEcheance(dto.getDateEcheance());

        // ✔ statut Enum
        if (dto.getStatut() != null)
            p.setStatut(StatutPlanification.valueOf(dto.getStatut()));

        // ✔ résultat Enum
        if (dto.getResultat() != null)
            p.setResultat(ResultatTraitement.valueOf(dto.getResultat()));

        p.setTypeSource(dto.getTypeSource());


        // ------------ DESTINATAIRE ------------
        if (dto.getDestinataireId() != null) {
            p.setDestinataire(
                    employeRepository.findById(dto.getDestinataireId())
                            .orElseThrow(() -> new RuntimeException("Destinataire introuvable"))
            );
        }


        // ------------ SOURCE COURRIER ------------
        if ("COURRIER".equals(dto.getTypeSource())) {

            if (dto.getCourrierEntrantId() != null) {
                p.setCourrierEntrant(
                        courrierEntrantRepository.findById(dto.getCourrierEntrantId())
                                .orElseThrow(() -> new RuntimeException("Courrier entrant introuvable"))
                );
            }

            if (dto.getCourrierSortantId() != null) {
                p.setCourrierSortant(
                        courrierSortantRepository.findById(dto.getCourrierSortantId())
                                .orElseThrow(() -> new RuntimeException("Courrier sortant introuvable"))
                );
            }
        }


        // ------------ TRANSMISSION D’ORIGINE ------------
        if (dto.getTransmissionId() != null) {
            p.setTransmission(
                    transmissionRepository.findById(dto.getTransmissionId())
                            .orElseThrow(() -> new RuntimeException("Transmission introuvable"))
            );
        }


        // ------------ PIÈCES JOINTES ------------
        if (dto.getPiecesJointesIds() != null) {
            List<PieceJointe> pieces =
                    pieceJointeRepository.findAllById(dto.getPiecesJointesIds());
            p.setPiecesJointes(pieces);
        }

        return planificationRepository.save(p);
    }



    // =========================
    //        GET ALL
    // =========================
    public List<PlanificationDTO> getAll() {
        return planificationRepository.findAll()
                .stream()
                .map(PlanificationMapper::toDTO)
                .collect(Collectors.toList());
    }



    // =========================
    //        GET BY ID
    // =========================
    public PlanificationDTO getById(Long id) {
        return planificationRepository.findById(id)
                .map(PlanificationMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));
    }



    // =========================
    //        UPDATE
    // =========================
    public Planification update(Long id, PlanificationDTO dto) {

        Planification p = planificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));

        p.setMessage(dto.getMessage());
        p.setDateEcheance(dto.getDateEcheance());

        if (dto.getStatut() != null)
            p.setStatut(StatutPlanification.valueOf(dto.getStatut()));

        if (dto.getResultat() != null)
            p.setResultat(ResultatTraitement.valueOf(dto.getResultat()));
        
        if (dto.getTransmissionId() != null) {
            p.setTransmission(
                transmissionRepository.findById(dto.getTransmissionId())
                    .orElseThrow(() -> new RuntimeException("Transmission introuvable"))
            );
        }

        if (dto.getPiecesJointesIds() != null) {
            List<PieceJointe> pieces =
                pieceJointeRepository.findAllById(dto.getPiecesJointesIds());
            p.setPiecesJointes(pieces);
        }

        p.setTypeSource(dto.getTypeSource());


        // destinataire
        if (dto.getDestinataireId() != null) {
            p.setDestinataire(
                    employeRepository.findById(dto.getDestinataireId())
                            .orElseThrow(() -> new RuntimeException("Destinataire introuvable"))
            );
        }

        return planificationRepository.save(p);
    }



    // =========================
    //        DELETE
    // =========================
    public void delete(Long id) {
        planificationRepository.deleteById(id);
    }



    // =========================
    //  CRÉER PLANIFICATION DEPUIS TRANSMISSION
    // =========================
    public void creerDepuisTransmission(TransmissionCourrier t) {

        Planification p = new Planification();

        p.setTypeSource(TypeSourcePlanification.COURRIER);
        p.setMessage(t.getMessage());

        // ⚠ TransmissionCourrier n'a PAS dateEcheance → enlever !
        p.setDateEcheance(null);

        p.setStatut(StatutPlanification.ENVOYE);

        // courrier
        if (t.getCourrierEntrant() != null)
            p.setCourrierEntrant(t.getCourrierEntrant());

        if (t.getCourrierSortant() != null)
            p.setCourrierSortant(t.getCourrierSortant());

        // destinataire
        p.setDestinataire(t.getDestinataire());

        // rattacher transmission
        p.setTransmission(t);

        planificationRepository.save(p);
    }
    public void repondrePlanification(Long planId, ResultatTraitement resultat, String message) {

        Planification p = planificationRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));

        // réponse employé
        p.setResultat(resultat);
        p.setMessage(message);

        // dès qu’il y a une réponse → statut REPONDU
        if (resultat != null) {
            p.setStatut(StatutPlanification.REPONDU);
        }

        planificationRepository.save(p);
    }
    public List<Planification> findByUserId(Long id) {
        return planificationRepository.findByDestinataireId(id);
    }
    public Planification getEntityById(Long id) {
        return planificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));
    }
    public List<PlanificationDTO> findByDestinataire(Long id) {

        List<Planification> list = planificationRepository.findByDestinataireId(id);

        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PlanificationDTO toDTO(Planification p) {

        PlanificationDTO dto = new PlanificationDTO();

        dto.setId(p.getId());
        dto.setMessage(p.getMessage());
        dto.setStatut(p.getStatut() != null ? p.getStatut().name() : null);
        dto.setResultat(p.getResultat() != null ? p.getResultat().name() : null);

        if (p.getDestinataire() != null) {
            dto.setDestinataireId(p.getDestinataire().getId());
            dto.setDestinataireNom(p.getDestinataire().getNom());
        }

        if (p.getTransmission() != null) {
            dto.setTransmissionId(p.getTransmission().getId());
        }

        return dto;
    }
    public Planification repondre(Long id, ResponsePlanificationDTO dto) {

        Planification p = planificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planification introuvable"));

        // 🟢 L'employé ajoute sa réponse
        p.setMessage(dto.getMessage());

        // 🟢 Il met un résultat (ACCEPTE / REFUSE / TRAITE)
        p.setResultat(dto.getResultat());

       

        return planificationRepository.save(p);
    }


	

}
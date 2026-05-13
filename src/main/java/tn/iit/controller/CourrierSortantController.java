package tn.iit.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.iit.dto.ApiResponse;
import tn.iit.dto.CourrierSortantDTO;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.Employe;
import tn.iit.entity.EtatCourrier;
import tn.iit.entity.ModeExpedition;
import tn.iit.entity.Priorite;
import tn.iit.entity.StatutCourrier;
import tn.iit.mapper.CourrierMapper;
import tn.iit.service.CompteurService;
import tn.iit.service.CourrierSortantService;
import tn.iit.service.EmployeService;

@RestController
@RequestMapping("/api/courriers-sortants")
@RequiredArgsConstructor
public class CourrierSortantController {

    private final CourrierSortantService courrierSortantService;
    private final CompteurService compteurService;
    private final CourrierMapper courrierMapper;
    private final EmployeService employeService;

    
    @GetMapping("/mes-courriers")
    public ResponseEntity<ApiResponse> getMesCourriers(Principal principal) {
        Employe employe = employeService.findByUsername(principal.getName());

        List<CourrierSortantDTO> courriers = courrierSortantService
                .findByEmploye(employe)
                .stream()
                .map(courrierMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Vos courriers sortants", courriers));
    }

    /* ===========================
        CRÉATION
       =========================== */
    @PostMapping
    public ResponseEntity<ApiResponse> createCourrierSortant(
            @Valid @RequestBody CourrierSortantDTO dto,
            Principal principal) {

        try {
            String numeroOrdre = compteurService.genererNumero("CS");

            CourrierSortant entity = courrierMapper.toEntity(dto);
            entity.setNumeroOrdre(numeroOrdre);

            // Statut et etat gérés par l’entity → ne rien modifier
            entity.setDateSaisie(LocalDate.now());

            // Employé connecté
            Employe employe = employeService.findByUsername(principal.getName());
            entity.setEmploye(employe);

            CourrierSortant saved = courrierSortantService.save(entity);

            return ResponseEntity.ok(
                    ApiResponse.success("Courrier sortant créé avec succès", courrierMapper.toDTO(saved))
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /* ===========================
        GET BY ID (sécurisé)
       =========================== */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCourrierSortantById(
            @PathVariable Long id,
            Principal principal) {

        Employe employe = employeService.findByUsername(principal.getName());

        return courrierSortantService.findById(id)
                .filter(c -> c.getEmploye().getId().equals(employe.getId()))
                .map(c -> ResponseEntity.ok(
                        ApiResponse.success("Courrier trouvé", courrierMapper.toDTO(c))
                ))
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("Accès refusé ou courrier introuvable")));
    }

    /* ===========================
        UPDATE (sécurisé)
       =========================== */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCourrierSortant(
            @PathVariable Long id,
            @Valid @RequestBody CourrierSortantDTO dto,
            Principal principal) {

        Employe employe = employeService.findByUsername(principal.getName());

        return courrierSortantService.findById(id)
                .filter(c -> c.getEmploye().getId().equals(employe.getId()))
                .map(existing -> {

                    // ❌ Blocage si transmis
                    if (!existing.getTransmissions().isEmpty()) {
                        return ResponseEntity.badRequest()
                                .body(ApiResponse.error("Impossible de modifier : courrier déjà transmis"));
                    }

                    existing.setDateEmission(dto.getDateEmission());
                    existing.setTypeDocument(dto.getTypeDocument());
                    existing.setReference(dto.getReference());
                    existing.setNature(dto.getNature());
                    existing.setDestinataire(dto.getDestinataire());

                    // Enums
                    existing.setModeExpedition(ModeExpedition.valueOf(dto.getModeExpedition()));
                    existing.setStatut(StatutCourrier.valueOf(dto.getStatut()));
                    existing.setPriorite(Priorite.valueOf(dto.getPriorite()));

                    existing.setAdresseLivraison(dto.getAdresseLivraison());
                    existing.setEmailDestinataire(dto.getEmailDestinataire());

                    CourrierSortant updated = courrierSortantService.save(existing);

                    return ResponseEntity.ok(ApiResponse.success(
                            "Courrier sortant modifié avec succès", courrierMapper.toDTO(updated)
                    ));
                })
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("Accès refusé ou courrier introuvable")));
    }

    /* ===========================
        DELETE (sécurisé)
       =========================== */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCourrierSortant(
            @PathVariable Long id,
            Principal principal) {

        Employe employe = employeService.findByUsername(principal.getName());

        return courrierSortantService.findById(id)
            .filter(c -> c.getEmploye().getId().equals(employe.getId()))
            .map(courrier -> {

                // ❌ Empêcher suppression si transmissions
                if (!courrier.getTransmissions().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Impossible de supprimer : courrier déjà transmis"));
                }

                // ✔ Soft delete via état
                courrier.setEtat(EtatCourrier.SUPPRIME);
                courrierSortantService.save(courrier);

                return ResponseEntity.ok(ApiResponse.success("Courrier supprimé"));
            })
            .orElse(ResponseEntity.badRequest()
                    .body(ApiResponse.error("Courrier non trouvé ou accès refusé")));
    }
    @PutMapping("/{id}/assigner/{employeId}")
    public ResponseEntity<CourrierSortant> assignerEmploye(
            @PathVariable Long id,
            @PathVariable Long employeId) {
        return ResponseEntity.ok(courrierSortantService.assignerEmploye(id, employeId));
    }

}

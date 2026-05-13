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
import tn.iit.dto.CourrierEntrantDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.Employe;
import tn.iit.entity.EtatCourrier;
import tn.iit.entity.ModeReception;
import tn.iit.entity.Priorite;
import tn.iit.entity.StatutCourrier;
import tn.iit.mapper.CourrierMapper;
import tn.iit.service.CompteurService;
import tn.iit.service.CourrierEntrantService;
import tn.iit.service.EmployeService;


@RestController
@RequestMapping("/api/courriers-entrants")
@RequiredArgsConstructor
public class CourrierEntrantController {

	private final CourrierEntrantService courrierEntrantService;
	private final CompteurService compteurService;
	private final CourrierMapper courrierMapper;
	private final EmployeService employeService; // service pour récupérer l'employé connecté

	// Récupérer les courriers de l'employé connecté
	@GetMapping("/mes-courriers")
	public ResponseEntity<ApiResponse> getMesCourriers(Principal principal) {
		Employe employe = employeService.findByUsername(principal.getName());
		List<CourrierEntrantDTO> courriers = courrierEntrantService.findByEmploye(employe).stream()
				.map(courrierMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok(ApiResponse.success("Vos courriers entrants", courriers));
	}

	// Création sécurisée : le courrier est automatiquement lié à l'employé connecté
	@PostMapping
	public ResponseEntity<ApiResponse> createCourrierEntrant(
	        @Valid @RequestBody CourrierEntrantDTO courrierDTO,
	        Principal principal) {

	    try {
	        String numeroOrdre = compteurService.genererNumero("CE");

	        CourrierEntrant courrier = courrierMapper.toEntity(courrierDTO);
	        courrier.setNumeroOrdre(numeroOrdre);

	        // Statut + etat = valeurs par défaut via entity
	        courrier.setStatut(StatutCourrier.NOUVEAU);
	        courrier.setEtat(EtatCourrier.ACTIVE);

	        courrier.setDateSaisie(LocalDate.now());

	        // Employé connecté
	        Employe employe = employeService.findByUsername(principal.getName());
	        courrier.setEmploye(employe);

	        CourrierEntrant saved = courrierEntrantService.save(courrier);

	        return ResponseEntity.ok(
	                ApiResponse.success("Courrier entrant créé avec succès",
	                        courrierMapper.toDTO(saved)));

	    } catch (Exception e) {
	        return ResponseEntity.badRequest()
	                .body(ApiResponse.error("Erreur lors de la création du courrier : " + e.getMessage()));
	    }
	}

	// Récupération par ID : vérifier que le courrier appartient à l'employé
	// connecté
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getCourrierEntrantById(
	        @PathVariable Long id, Principal principal) {

	    Employe employe = employeService.findByUsername(principal.getName());

	    return courrierEntrantService.findById(id)
	            .filter(c -> c.getEmploye().getId().equals(employe.getId()))
	            .map(courrier -> ResponseEntity.ok(ApiResponse.success(
	                    "Courrier trouvé", courrierMapper.toDTO(courrier))))
	            .orElse(ResponseEntity.badRequest()
	                    .body(ApiResponse.error("Courrier non trouvé ou accès refusé")));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateCourrierEntrant(
	        @PathVariable Long id,
	        @Valid @RequestBody CourrierEntrantDTO dto,
	        Principal principal) {

	    Employe employe = employeService.findByUsername(principal.getName());

	    return courrierEntrantService.findById(id)
	            .filter(c -> c.getEmploye().getId().equals(employe.getId()))
	            .map(existing -> {

	                // Empêcher modification si transmis
	                if (!existing.getTransmissions().isEmpty()) {
	                    return ResponseEntity.badRequest()
	                            .body(ApiResponse.error("Impossible de modifier : courrier déjà transmis"));
	                }

	                existing.setDateReception(dto.getDateReception());
	                existing.setTypeDocument(dto.getTypeDocument());
	                existing.setReference(dto.getReference());
	                existing.setNature(dto.getNature());
	                existing.setExpediteur(dto.getExpediteur());

	                existing.setModeReception(ModeReception.valueOf(dto.getModeReception()));
	                existing.setStatut(StatutCourrier.valueOf(dto.getStatut()));
	                existing.setPriorite(Priorite.valueOf(dto.getPriorite()));

	                CourrierEntrant updated = courrierEntrantService.save(existing);

	                return ResponseEntity.ok(
	                        ApiResponse.success("Courrier modifié", courrierMapper.toDTO(updated)));

	            }).orElse(ResponseEntity.badRequest()
	                    .body(ApiResponse.error("Courrier non trouvé ou accès refusé")));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteCourrierEntrant(
	        @PathVariable Long id, Principal principal) {

	    Employe employe = employeService.findByUsername(principal.getName());

	    return courrierEntrantService.findById(id)
	            .filter(c -> c.getEmploye().getId().equals(employe.getId()))
	            .map(courrier -> {

	                // Empêcher suppression si transmis
	                if (!courrier.getTransmissions().isEmpty()) {
	                    return ResponseEntity.badRequest()
	                            .body(ApiResponse.error("Impossible de supprimer : courrier transmis"));
	                }

	                // Soft delete → on ne touche pas aux FK
	                courrier.setEtat(EtatCourrier.SUPPRIME);

	                courrierEntrantService.save(courrier);

	                return ResponseEntity.ok(
	                        ApiResponse.success("Courrier supprimé"));

	            }).orElse(ResponseEntity.badRequest()
	                    .body(ApiResponse.error("Courrier non trouvé ou accès refusé")));
	}
	@PutMapping("/{id}/assigner/{employeId}")
	public ResponseEntity<CourrierEntrant> assignerEmploye(
	        @PathVariable Long id,
	        @PathVariable Long employeId) {
	    return ResponseEntity.ok(courrierEntrantService.assignerEmploye(id, employeId));
	}

	
}

package tn.iit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.ApiResponse;
import tn.iit.dto.TransmissionDTO;
import tn.iit.dto.TransmissionRequest;
import tn.iit.entity.Employe;

import tn.iit.entity.TransmissionCourrier;
import tn.iit.mapper.TransmissionMapper;

import tn.iit.service.EmployeService;
import tn.iit.service.TransmissionCourrierService;

@RestController
@RequestMapping("/api/transmissions")
@RequiredArgsConstructor
public class TransmissionCourrierController {

    private final TransmissionCourrierService service;
    private final EmployeService employeService;

    // =========================
    // ENVOYER
    // =========================
    @PreAuthorize("hasRole('RESPONSABLE')")
    @PostMapping("/envoyer")
    public ResponseEntity<ApiResponse> envoyer(
            @RequestBody TransmissionRequest request,
            Authentication auth) {

        if (request.getCourrierId() == null || request.getType() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("courrierId et type sont obligatoires"));
        }

        

        TransmissionCourrier saved = service.envoyerCourrier(request);

        return ResponseEntity.ok(
                ApiResponse.success("Transmission envoyée", TransmissionMapper.toDTO(saved))
        );
    }
    // =========================
    // REÇUS
    // =========================
    @GetMapping("/recus")
    public ResponseEntity<ApiResponse> recus(Authentication auth) {

        Employe user = employeService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<TransmissionDTO> data = service.getRecus(user.getId())
                .stream()
                .map(TransmissionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Liste des transmissions reçues", data)
        );
    }

   

    // =========================
    // MARQUER LU
    // =========================
    @PutMapping("/{id}/lu")
    public ResponseEntity<ApiResponse> marquerLu(@PathVariable Long id) {

        TransmissionCourrier updated = service.marquerCommeLu(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Transmission marquée comme lue",
                        TransmissionMapper.toDTO(updated)
                )
        );
    }

   
    @GetMapping("/by-courrier-entrant/{id}")
    public ResponseEntity<ApiResponse> getByCourrierEntrant(@PathVariable Long id) {

        List<TransmissionDTO> data = service.getByCourrierEntrant(id)
                .stream()
                .map(TransmissionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Transmissions trouvées", data)
        );
    }
    @GetMapping("/by-courrier-sortant/{id}")
    public ResponseEntity<ApiResponse> getByCourrierSortant(@PathVariable Long id) {

    	List<TransmissionDTO> data = service.getByCourrierSortant(id)
    			 .stream()
                 .map(TransmissionMapper::toDTO)
                 .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Transmission trouvée", data)
        );
    }
}
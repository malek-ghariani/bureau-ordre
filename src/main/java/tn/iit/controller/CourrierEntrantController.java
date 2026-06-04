package tn.iit.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.iit.dto.ApiResponse;

import tn.iit.dto.CourrierEntrantDTO;

import tn.iit.service.CourrierEntrantService;



@RestController
@RequestMapping("/api/courriers-entrants")
@RequiredArgsConstructor
public class CourrierEntrantController {

    private final CourrierEntrantService courrierEntrantService;

    // 1. GET ALL — réservé responsable
    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success("Vos courriers entrants", courrierEntrantService.getAll())
        );
    }

    // 2. CREATE
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CourrierEntrantDTO dto) {
        return ResponseEntity.ok(
                ApiResponse.success("Courrier créé", courrierEntrantService.create(dto))
        );
    }

    @GetMapping("/archives")
    public ResponseEntity<ApiResponse> getArchives() {
        return ResponseEntity.ok(
                ApiResponse.success("Archives", courrierEntrantService.getArchives())
        );
    }
    // 3. GET BY ID — plus de contrôle employé/id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Courrier trouvé", courrierEntrantService.getById(id))
        );
    }

    // 4. UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CourrierEntrantDTO dto) {

        return ResponseEntity.ok(
                ApiResponse.success("Modifié", courrierEntrantService.update(id, dto))
        );
    }

    // 5. DELETE — soft delete simple
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {

        courrierEntrantService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Courrier supprimé"));
    }
    @PutMapping("/{id}/statut")
    public ResponseEntity<ApiResponse> changerStatut(
            @PathVariable Long id,
            @RequestParam String statut) {

        return ResponseEntity.ok(
                ApiResponse.success("Statut modifié", courrierEntrantService.changeStatut(id, statut))
        );
    }

    // 6. ARCHIVER
    @PutMapping("/{id}/archiver")
    public ResponseEntity<ApiResponse> archiver(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Courrier archivé", courrierEntrantService.archiver(id))
        );
    }
    
   
}
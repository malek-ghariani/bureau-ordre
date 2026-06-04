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


import lombok.RequiredArgsConstructor;
import tn.iit.dto.ApiResponse;
import tn.iit.dto.CourrierEntrantDTO;
import tn.iit.dto.CourrierSortantDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.StatutCourrier;
import tn.iit.service.CourrierSortantService;


@RestController
@RequestMapping("/api/courriers-sortants")
@RequiredArgsConstructor
public class CourrierSortantController {

    private final CourrierSortantService service;
   

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success("Liste", service.getAll())
        );
    }

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CourrierSortantDTO dto) {

        return ResponseEntity.ok(
                ApiResponse.success("Créé", service.create(dto))
        );
    }
    
    @GetMapping("/archives")
    public ResponseEntity<ApiResponse> archives() {
        return ResponseEntity.ok(
                ApiResponse.success("Archives", service.getArchives())
        );
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("OK", service.findById(id))
        );
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                             @RequestBody CourrierSortantDTO dto) {

        return ResponseEntity.ok(
                ApiResponse.success("Modifié", service.update(id, dto))
        );
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Supprimé"));
    }
    // 7. STATUT
    @PutMapping("/{id}/statut")
    public ResponseEntity<ApiResponse> changerStatut(
            @PathVariable Long id,
            @RequestParam String statut) {

        return ResponseEntity.ok(
                ApiResponse.success("Statut modifié", service.changeStatut(id, statut))
        );
    }

    /* ================= ARCHIVER ================= */
    @PutMapping("/{id}/archiver")
    public ResponseEntity<ApiResponse> archiver(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Archivé", service.archiver(id))
        );
    }

  
}
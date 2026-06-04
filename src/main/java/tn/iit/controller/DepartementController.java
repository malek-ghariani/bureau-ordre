package tn.iit.controller;

import tn.iit.dto.ApiResponse;
import tn.iit.dto.DepartementDTO;

import tn.iit.service.DepartementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/departements")
@RequiredArgsConstructor
public class DepartementController {

    private final DepartementService departementService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllDepartements() {
        List<DepartementDTO> departements = departementService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("Liste des départements récupérée", departements)
        );
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse> getDepartementById(@PathVariable String code) {
        try {
            DepartementDTO dto = departementService.findById(code);
            return ResponseEntity.ok(
                    ApiResponse.success("Département trouvé", dto)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.ok(
                    ApiResponse.error("Département non trouvé")
            );
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createDepartement(@Valid @RequestBody DepartementDTO dto) {

        if (departementService.existsByCode(dto.getCode())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Code département déjà utilisé"));
        }

        DepartementDTO saved = departementService.save(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Département créé avec succès", saved)
        );
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse> updateDepartement(
            @PathVariable String code,
            @Valid @RequestBody DepartementDTO dto) {

        if (!departementService.existsByCode(code)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Département non trouvé"));
        }

        dto.setCode(code);
        DepartementDTO updated = departementService.save(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Département modifié avec succès", updated)
        );
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse> deleteDepartement(@PathVariable String code) {

        if (!departementService.existsByCode(code)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Département non trouvé"));
        }

        departementService.deleteById(code);

        return ResponseEntity.ok(
                ApiResponse.success("Département supprimé avec succès")
        );
    }
}
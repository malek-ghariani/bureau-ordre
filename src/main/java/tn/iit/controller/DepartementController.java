package tn.iit.controller;

import tn.iit.dto.ApiResponse;
import tn.iit.dto.DepartementDTO;
import tn.iit.entity.Departement;
import tn.iit.service.DepartementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departements")
@RequiredArgsConstructor
public class DepartementController {

    private final DepartementService departementService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllDepartements() {
        List<DepartementDTO> departements = departementService.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Liste des départements récupérée", departements));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse> getDepartementById(@PathVariable String code) {
        return departementService.findById(code)
                .map(departement -> ResponseEntity.ok(ApiResponse.success("Département trouvé", toDTO(departement))))
                .orElse(ResponseEntity.ok(ApiResponse.error("Département non trouvé")));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createDepartement(@Valid @RequestBody DepartementDTO departementDTO) {
        if (departementService.existsByCode(departementDTO.getCode())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Code département déjà utilisé"));
        }

       
        Departement departement = toEntity(departementDTO);
        Departement savedDepartement = departementService.save(departement);
        return ResponseEntity.ok(ApiResponse.success("Département créé avec succès", toDTO(savedDepartement)));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse> updateDepartement(@PathVariable String code, @Valid @RequestBody DepartementDTO departementDTO) {
        return departementService.findById(code)
                .map(existingDepartement -> {
                    departementDTO.setCode(code);
                    Departement departementToUpdate = toEntity(departementDTO);
                    Departement updatedDepartement = departementService.save(departementToUpdate);
                    return ResponseEntity.ok(ApiResponse.success("Département modifié avec succès", toDTO(updatedDepartement)));
                })
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("Département non trouvé")));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse> deleteDepartement(@PathVariable String code) {
        return departementService.findById(code)
                .map(existingDepartement -> {
                    departementService.deleteById(code);
                    return ResponseEntity.ok(ApiResponse.success("Département supprimé avec succès"));
                })
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("Département non trouvé")));
    }

    
    private DepartementDTO toDTO(Departement departement) {
        DepartementDTO dto = new DepartementDTO();
        dto.setCode(departement.getCode());
        dto.setNom(departement.getNom());
        dto.setChefDepartement(departement.getChefDepartement());
        dto.setEmail(departement.getEmail());
        dto.setTelephone(departement.getTelephone());
        return dto;
    }

    private Departement toEntity(DepartementDTO dto) {
        Departement departement = new Departement();
        departement.setCode(dto.getCode());
        departement.setNom(dto.getNom());
        departement.setChefDepartement(dto.getChefDepartement());
        departement.setEmail(dto.getEmail());
        departement.setTelephone(dto.getTelephone());
        return departement;
    }
}

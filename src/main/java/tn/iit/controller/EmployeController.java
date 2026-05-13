package tn.iit.controller;

import tn.iit.dto.ApiResponse;
import tn.iit.dto.EmployeDTO;
import tn.iit.entity.Employe;
import tn.iit.entity.Departement;
import tn.iit.mapper.EmployeMapper;
import tn.iit.service.EmployeService;
import tn.iit.service.DepartementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeService employeService;
    private final DepartementService departementService;
    private final EmployeMapper employeMapper;

    
    @GetMapping
    public ResponseEntity<ApiResponse> getAllEmployes() {
        List<EmployeDTO> employes = employeService.findAll().stream()
                .map(employeMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Liste des employés récupérée", employes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployeById(@PathVariable Long id) {
        return employeService.findById(id)
                .map(employe -> ResponseEntity.ok(ApiResponse.success("Employé trouvé", employeMapper.toDTO(employe))))
                .orElse(ResponseEntity.ok(ApiResponse.error("Employé non trouvé")));
    }

    @GetMapping("/departement/{codeDepartement}")
    public ResponseEntity<ApiResponse> getEmployesByDepartement(@PathVariable String codeDepartement) {
        List<EmployeDTO> employes = employeService.findByDepartement(codeDepartement).stream()
                .map(employeMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Employés du département récupérés", employes));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createEmploye(@Valid @RequestBody EmployeDTO employeDTO) {
        if (employeService.existsByEmail(employeDTO.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email déjà utilisé"));
        }
        if (employeService.existsByMatricule(employeDTO.getMatricule())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Matricule déjà utilisé"));
        }

        Departement departement = departementService.findById(employeDTO.getDepartementCode()).orElse(null);
        if (departement == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Département non trouvé"));
        }

        Employe employe = employeMapper.toEntity(employeDTO);
        employe.setDepartement(departement);

        Employe savedEmploye = employeService.save(employe);
        return ResponseEntity.ok(ApiResponse.success("Employé créé avec succès", employeMapper.toDTO(savedEmploye)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateEmploye(@PathVariable Long id, @Valid @RequestBody EmployeDTO employeDTO) {
        return employeService.findById(id).map(existingEmploye -> {
            employeDTO.setId(id);

            if (!existingEmploye.getEmail().equals(employeDTO.getEmail())
                    && employeService.existsByEmail(employeDTO.getEmail())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Email déjà utilisé par un autre employé"));
            }

            Departement departement = departementService.findById(employeDTO.getDepartementCode())
                    .orElseThrow(() -> new RuntimeException("Département non trouvé"));

            Employe employeToUpdate = employeMapper.toEntity(employeDTO);
            employeToUpdate.setDepartement(departement);

            Employe updatedEmploye = employeService.save(employeToUpdate);
            return ResponseEntity.ok(ApiResponse.success("Employé modifié avec succès", employeMapper.toDTO(updatedEmploye)));
        }).orElse(ResponseEntity.badRequest().body(ApiResponse.error("Employé non trouvé")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteEmploye(@PathVariable Long id) {
        return employeService.findById(id).map(existingEmploye -> {
            employeService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Employé supprimé avec succès"));
        }).orElse(ResponseEntity.badRequest().body(ApiResponse.error("Employé non trouvé")));
    }
}

package tn.iit.controller;

import tn.iit.dto.ApiResponse;
import tn.iit.dto.EmployeDTO;
import tn.iit.entity.Employe;

import tn.iit.mapper.EmployeMapper;
import tn.iit.service.EmployeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeService employeService;
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
            .map(employe -> ResponseEntity.ok(
                ApiResponse.success("Employé trouvé", employeMapper.toDTO(employe))))
            .orElse(ResponseEntity.badRequest()
                .body(ApiResponse.error("Employé non trouvé")));
    }

    @GetMapping("/departement/{codeDepartement}")
    public ResponseEntity<ApiResponse> getEmployesByDepartement(@PathVariable String codeDepartement) {
        List<EmployeDTO> employes = employeService.findByDepartement(codeDepartement).stream()
            .map(employeMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Employés du département récupérés", employes));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createEmploye(@Valid @RequestBody EmployeDTO employeDTO) {
        // ✅ vérifications email et matricule
        if (employeService.existsByEmail(employeDTO.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email déjà utilisé"));
        }
        if (employeService.existsByMatricule(employeDTO.getMatricule())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Matricule déjà utilisé"));
        }

        Employe employe = employeMapper.toEntity(employeDTO); 
        Employe saved = employeService.create(employe);       
        return ResponseEntity.ok(ApiResponse.success("Employé créé avec succès", employeMapper.toDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateEmploye(
            @PathVariable Long id,
            @Valid @RequestBody EmployeDTO employeDTO) {

        return employeService.findById(id).map(existingEmploye -> {

            // ✅ vérifier email si modifié
            if (!existingEmploye.getEmail().equals(employeDTO.getEmail())
                    && employeService.existsByEmail(employeDTO.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email déjà utilisé par un autre employé"));
            }

            // ✅ vérifier matricule si modifié
            if (!existingEmploye.getMatricule().equals(employeDTO.getMatricule())
                    && employeService.existsByMatricule(employeDTO.getMatricule())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Matricule déjà utilisé par un autre employé"));
            }

            employeDTO.setId(id);
            Employe employe = employeMapper.toEntity(employeDTO); // ✅ mapper gère le département
            Employe updated = employeService.update(employe);     // ✅ pas de réencodage
            return ResponseEntity.ok(ApiResponse.success("Employé modifié avec succès", employeMapper.toDTO(updated)));

        }).orElse(ResponseEntity.badRequest().body(ApiResponse.error("Employé non trouvé")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmploye(@PathVariable Long id) {
        return employeService.findById(id).map(existingEmploye -> {
            employeService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Employé supprimé avec succès"));
        }).orElse(ResponseEntity.badRequest().body(ApiResponse.error("Employé non trouvé")));
    }
    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updatePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String newPassword = body.get("newPassword");

        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Le nouveau mot de passe est obligatoire"));
        }

        employeService.updatePassword(id, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Mot de passe modifié avec succès"));
    }
}
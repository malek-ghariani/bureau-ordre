package tn.iit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.PlanificationDTO;
import tn.iit.dto.ResponsePlanificationDTO;
import tn.iit.entity.Employe;
import tn.iit.service.EmployeService;
import tn.iit.service.PlanificationService;

@RestController
@RequestMapping("/api/planifications")
@RequiredArgsConstructor
public class PlanificationController {

    private final PlanificationService service;
    private final EmployeService employeService;

   
    // GET ALL
    @PreAuthorize("hasRole('RESPONSABLE')")
    @GetMapping
    public ResponseEntity<List<PlanificationDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PlanificationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // UPDATE
    @PreAuthorize("hasRole('RESPONSABLE')")
    @PutMapping("/{id}")
    public ResponseEntity<PlanificationDTO> update(
            @PathVariable Long id,
            @RequestBody PlanificationDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    // DELETE
    @PreAuthorize("hasRole('RESPONSABLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // REPONSE EMPLOYE
    @PreAuthorize("hasRole('AGENT')")
    @PutMapping("/{id}/repondre")
    public ResponseEntity<PlanificationDTO> repondre(
            @PathVariable Long id,
            @RequestBody ResponsePlanificationDTO dto) {

        return ResponseEntity.ok(
                service.repondre(id, dto)
        );
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/mes-planifications")
    public ResponseEntity<List<PlanificationDTO>> getMesPlanifications(Authentication auth) {
        Employe agent = employeService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return ResponseEntity.ok(service.findByDestinataire(agent.getId()));
    }
    
}
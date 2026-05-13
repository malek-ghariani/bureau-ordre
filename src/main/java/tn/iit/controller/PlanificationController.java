package tn.iit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.PlanificationDTO;
import tn.iit.dto.ResponsePlanificationDTO;
import tn.iit.entity.Planification;
import tn.iit.mapper.PlanificationMapper;
import tn.iit.repository.PlanificationRepository;
import tn.iit.service.PlanificationService;

@RestController
@RequestMapping("/api/planifications")
@RequiredArgsConstructor
public class PlanificationController {

	private PlanificationRepository planificationRepository ;
    private final PlanificationService service;

    // CREATE
    @PostMapping
    public ResponseEntity<PlanificationDTO> create(@RequestBody PlanificationDTO dto) {
        return ResponseEntity.ok(
                PlanificationMapper.toDTO(service.create(dto))
        );
    }

    // GET ALL
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
    @PutMapping("/{id}")
    public ResponseEntity<PlanificationDTO> update(
            @PathVariable Long id,
            @RequestBody PlanificationDTO dto) {

        return ResponseEntity.ok(
                PlanificationMapper.toDTO(service.update(id, dto))
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/repondre")
    public ResponseEntity<PlanificationDTO> repondre(
            @PathVariable Long id,
            @RequestBody ResponsePlanificationDTO dto) {

        service.repondrePlanification(id, dto.getResultat(), dto.getMessage());

        Planification updated = service.getEntityById(id);

        return ResponseEntity.ok(PlanificationMapper.toDTO(updated));
    }
    @GetMapping("/employe/{id}")
    public List<PlanificationDTO> getByDestinataire(@PathVariable Long id) {
        return service.findByDestinataire(id);
    }

}
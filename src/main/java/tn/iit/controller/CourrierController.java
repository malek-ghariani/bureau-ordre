package tn.iit.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.CourrierDTO;
import tn.iit.entity.Employe;
import tn.iit.mapper.CourrierMapper;
import tn.iit.service.CourrierEntrantService;
import tn.iit.service.CourrierSortantService;
import tn.iit.service.EmployeService;

@RestController
@RequestMapping("/api/courriers")
@RequiredArgsConstructor
public class CourrierController {

    private final CourrierEntrantService courrierEntrantService;
    private final CourrierSortantService courrierSortantService;
    private final CourrierMapper courrierMapper;
    private final EmployeService employeService;

    @GetMapping("/mes-courriers")
    public ResponseEntity<List<CourrierDTO>> getMesCourriers(Principal principal) {
        Employe employe = employeService.findByUsername(principal.getName());

        List<CourrierDTO> courriers = new ArrayList<>();

        // Courriers entrants assignés à l'employé
        courriers.addAll(
        	    courrierEntrantService.findByEmploye(employe)
        	        .stream()
        	        .map(c -> courrierMapper.toUnifiedDTO(c, "ENTRANT"))
        	        .collect(Collectors.toList())
        	);

        // Courriers sortants assignés à l'employé
        courriers.addAll(
        	    courrierSortantService.findByEmploye(employe)
        	        .stream()
        	        .map(c -> courrierMapper.toUnifiedDTO(c, "SORTANT"))
        	        .collect(Collectors.toList())
        	);
        return ResponseEntity.ok(courriers);
    }
}

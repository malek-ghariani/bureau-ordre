package tn.iit.controller;

import tn.iit.dto.ApiResponse;
import tn.iit.service.CourrierEntrantService;
import tn.iit.service.CourrierSortantService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final CourrierEntrantService courrierEntrantService;
    private final CourrierSortantService courrierSortantService;
    
    @GetMapping("/statistiques")
    public ResponseEntity<ApiResponse> getStatistiques(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();
        
        Map<String, Object> statistiques = new HashMap<>();
        
       
        statistiques.put("totalCourriersEntrants", courrierEntrantService.findByDateReceptionBetween(startDate, endDate).size());
        statistiques.put("courriersEntrantsNouveaux", courrierEntrantService.findByStatut("NOUVEAU").size());
        statistiques.put("courriersEntrantsTraites", courrierEntrantService.findByStatut("TRAITE").size());
        
        
        statistiques.put("totalCourriersSortants", courrierSortantService.findByDateEmissionBetween(startDate, endDate).size());
        statistiques.put("courriersSortantsBrouillon", courrierSortantService.findByStatut("BROUILLON").size());
        statistiques.put("courriersSortantsExpedies", courrierSortantService.findByStatut("EXPEDIE").size());
        
        return ResponseEntity.ok(ApiResponse.success("Statistiques récupérées", statistiques));
    }
    
    @GetMapping("/today")
    public ResponseEntity<ApiResponse> getTodayStats() {
        LocalDate today = LocalDate.now();
        
        Map<String, Object> todayStats = new HashMap<>();
        todayStats.put("courriersEntrantsAujourdhui", courrierEntrantService.countCourriersByDate(today));
        todayStats.put("courriersSortantsAujourdhui", courrierSortantService.countCourriersByDate(today));
        
        return ResponseEntity.ok(ApiResponse.success("Statistiques du jour", todayStats));
    }
}
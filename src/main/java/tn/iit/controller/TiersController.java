package tn.iit.controller;

import tn.iit.dto.ApiResponse;
import tn.iit.dto.TiersDTO;
import tn.iit.entity.Tiers;
import tn.iit.service.TiersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tiers")
@RequiredArgsConstructor
public class TiersController {
    
    private final TiersService tiersService;
    
    @GetMapping
    public ResponseEntity<ApiResponse> getAllTiers() {
        List<TiersDTO> tiers = tiersService.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Liste des tiers récupérée", tiers));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTiersById(@PathVariable Long id) {
        return tiersService.findById(id)
                .map(tiers -> ResponseEntity.ok(ApiResponse.success("Tiers trouvé", toDTO(tiers))))
                .orElse(ResponseEntity.ok(ApiResponse.error("Tiers non trouvé")));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse> createTiers(@Valid @RequestBody TiersDTO tiersDTO) {
        Tiers tiers = toEntity(tiersDTO);
        Tiers savedTiers = tiersService.save(tiers);
        return ResponseEntity.ok(ApiResponse.success("Tiers créé avec succès", toDTO(savedTiers)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTiers(@PathVariable Long id, @Valid @RequestBody TiersDTO tiersDTO) {
        if (!tiersService.findById(id).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Tiers non trouvé"));
        }
        
        tiersDTO.setId(id);
        Tiers tiers = toEntity(tiersDTO);
        Tiers updatedTiers = tiersService.save(tiers);
        return ResponseEntity.ok(ApiResponse.success("Tiers modifié avec succès", toDTO(updatedTiers)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTiers(@PathVariable Long id) {
        if (!tiersService.findById(id).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Tiers non trouvé"));
        }
        
        tiersService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Tiers supprimé avec succès"));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchTiers(@RequestParam String keyword) {
        List<TiersDTO> tiers = tiersService.searchTiers(keyword)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Résultats de recherche", tiers));
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse> getTiersByType(@PathVariable String type) {
        List<TiersDTO> tiers = tiersService.findByType(type)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Tiers par type récupérés", tiers));
    }
    
    private TiersDTO toDTO(Tiers tiers) {
        TiersDTO dto = new TiersDTO();
        dto.setId(tiers.getId());
        dto.setNom(tiers.getNom());
        dto.setTelephone(tiers.getTelephone());
        dto.setAdresse(tiers.getAdresse());
        dto.setEmail(tiers.getEmail());
        
        dto.setType(tiers.getType());
        dto.setNomContact(tiers.getNomContact());
       
        return dto;
    }
    
    private Tiers toEntity(TiersDTO dto) {
        Tiers tiers = new Tiers();
        tiers.setId(dto.getId());
        tiers.setNom(dto.getNom());
        tiers.setTelephone(dto.getTelephone());
        tiers.setAdresse(dto.getAdresse());
        tiers.setEmail(dto.getEmail());
      
        tiers.setType(dto.getType());
        tiers.setNomContact(dto.getNomContact());
       
        return tiers;
    }
}
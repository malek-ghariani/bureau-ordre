package tn.iit.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.iit.dto.ApiResponse;
import tn.iit.dto.TiersDTO;
import tn.iit.mapper.TiersMapper;
import tn.iit.entity.Tiers;
import tn.iit.service.TiersService;

@RestController
@RequestMapping("/api/tiers")
@RequiredArgsConstructor
public class TiersController {
    
    private final TiersService tiersService;
    private final TiersMapper tiersMapper;
    
    @GetMapping
    public ResponseEntity<ApiResponse> getAllTiers() {
        List<TiersDTO> tiers = tiersService.findAll()
                .stream()
                .map(tiersMapper::toDTO)  // ← remplace this::toDTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Liste des tiers récupérée", tiers));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTiersById(@PathVariable Long id) {
        return tiersService.findById(id)
                .map(tiers -> ResponseEntity.ok(ApiResponse.success("Tiers trouvé", tiersMapper.toDTO(tiers))))
                .orElse(ResponseEntity.ok(ApiResponse.error("Tiers non trouvé")));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse> createTiers(@Valid @RequestBody TiersDTO tiersDTO) {
        Tiers tiers = tiersMapper.toEntity(tiersDTO);
        Tiers savedTiers = tiersService.save(tiers);
        return ResponseEntity.ok(ApiResponse.success("Tiers créé avec succès", tiersMapper.toDTO(savedTiers)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTiers(
            @PathVariable Long id, 
            @Valid @RequestBody TiersDTO tiersDTO) {
        if (!tiersService.findById(id).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Tiers non trouvé"));
        }
        tiersDTO.setId(id);
        Tiers tiers = tiersMapper.toEntity(tiersDTO);
        Tiers updatedTiers = tiersService.save(tiers);
        return ResponseEntity.ok(ApiResponse.success("Tiers modifié avec succès", tiersMapper.toDTO(updatedTiers)));
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
                .map(tiersMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Résultats de recherche", tiers));
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse> getTiersByType(@PathVariable String type) {
        List<TiersDTO> tiers = tiersService.findByType(type)
                .stream()
                .map(tiersMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Tiers par type récupérés", tiers));
    }
    
  
}
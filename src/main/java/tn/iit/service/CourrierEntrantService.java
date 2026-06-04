package tn.iit.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.ArchiveEntrantDTO;
import tn.iit.dto.CourrierEntrantDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.EtatCourrier;

import tn.iit.entity.Priorite;
import tn.iit.entity.StatutCourrier;
import tn.iit.entity.Tiers;
import tn.iit.mapper.CourrierMapper;
import tn.iit.repository.CourrierEntrantRepository;
import tn.iit.repository.TiersRepository;

@Service
@RequiredArgsConstructor
public class CourrierEntrantService {

    private final CourrierEntrantRepository courrierEntrantRepository;
    private final TiersRepository tiersRepository;
    private final CourrierMapper courrierMapper;
    private final CompteurService compteurService;

    // CREATE
    public CourrierEntrantDTO create(CourrierEntrantDTO dto) {

        CourrierEntrant entity = courrierMapper.toEntity(dto);

        entity.setNumeroOrdre(compteurService.genererNumero("CE"));
        entity.setStatut(StatutCourrier.NOUVEAU);
        entity.setEtat(EtatCourrier.ACTIVE);

        if (dto.getExpediteurId() != null) {
            Tiers exp = tiersRepository.findById(dto.getExpediteurId())
                    .orElseThrow(() -> new RuntimeException("Tiers introuvable"));
            entity.setExpediteur(exp);
        }

        return courrierMapper.toDTO(courrierEntrantRepository.save(entity));
    }

    // GET BY ID
    public CourrierEntrantDTO getById(Long id) {
        CourrierEntrant entity = courrierEntrantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        return courrierMapper.toDTO(entity);
    }

    // UPDATE
    public CourrierEntrantDTO update(Long id, CourrierEntrantDTO dto) {

        CourrierEntrant entity = courrierEntrantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        
        entity.setDateReception(dto.getDateReception());
        entity.setReference(dto.getReference());
        entity.setNature(dto.getNature());
        
        entity.setPriorite(Priorite.valueOf(dto.getPriorite()));

        if (dto.getExpediteurId() != null) {
            Tiers exp = tiersRepository.findById(dto.getExpediteurId())
                    .orElseThrow(() -> new RuntimeException("Tiers introuvable"));
            entity.setExpediteur(exp);
        }

        return courrierMapper.toDTO(courrierEntrantRepository.save(entity));
    }

    // GET ALL
    public List<CourrierEntrantDTO> getAll() {
        return courrierEntrantRepository.findAll()
                .stream()
                .map(courrierMapper::toDTO)
                .collect(Collectors.toList());
    }

    // DELETE 
    public void delete(Long id) {
        CourrierEntrant entity = courrierEntrantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        courrierEntrantRepository.delete(entity);
    }

    // ARCHIVER
    public CourrierEntrantDTO archiver(Long id) {
        CourrierEntrant entity = courrierEntrantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        entity.setEtat(EtatCourrier.ARCHIVE);
        entity.setDateArchivage(LocalDateTime.now());

        return courrierMapper.toDTO(courrierEntrantRepository.save(entity));
    }

    // STATUT
    public CourrierEntrantDTO changeStatut(Long id, String statut) {
        CourrierEntrant entity = courrierEntrantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        entity.setStatut(StatutCourrier.valueOf(statut.toUpperCase()));

        return courrierMapper.toDTO(courrierEntrantRepository.save(entity));
    }

    // ARCHIVES
    public List<ArchiveEntrantDTO> getArchives() {
        return courrierEntrantRepository.findByEtat(EtatCourrier.ARCHIVE)
                .stream()
                .map(courrierMapper::toArchiveEntrantDTO)
                .collect(Collectors.toList());
    }
 // méthode interne pour les autres services/controllers qui ont besoin de l'entité
    public CourrierEntrant getEntityById(Long id) {
        return courrierEntrantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier entrant introuvable"));
    }
}
package tn.iit.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import tn.iit.dto.ArchiveSortantDTO;

import tn.iit.dto.CourrierSortantDTO;

import tn.iit.entity.CourrierSortant;

import tn.iit.entity.EtatCourrier;
import tn.iit.entity.ModeExpedition;
import tn.iit.entity.Priorite;
import tn.iit.entity.StatutCourrier;
import tn.iit.entity.Tiers;
import tn.iit.mapper.CourrierMapper;
import tn.iit.repository.CourrierSortantRepository;
import tn.iit.repository.TiersRepository;


@Service
@RequiredArgsConstructor
public class CourrierSortantService {

    private final CourrierSortantRepository courrierSortantRepository;
    private final TiersRepository tiersRepository;
    private final CompteurService compteurService;
    private final CourrierMapper courrierMapper;

    /**
     * 1. CREATION
     */
    public CourrierSortantDTO create(CourrierSortantDTO dto) {

        CourrierSortant entity = courrierMapper.toEntity(dto);

        // génération du numéro
        String numero = compteurService.genererNumero("CS");
        entity.setNumeroOrdre(numero);

        // valeurs par défaut
        entity.setStatut(StatutCourrier.NOUVEAU);
        entity.setEtat(EtatCourrier.ACTIVE);

        // destinataire
        if (dto.getDestinataireId() != null) {
            Tiers tiers = tiersRepository.findById(dto.getDestinataireId())
                    .orElseThrow(() -> new RuntimeException("Tiers introuvable"));
            entity.setDestinataire(tiers);
        }

        CourrierSortant saved = courrierSortantRepository.save(entity);

        return courrierMapper.toDTO(saved);
    }

    /**
     * 2. MODIFICATION
     */
 
    public CourrierSortantDTO update(Long id, CourrierSortantDTO dto) {

        CourrierSortant existing = courrierSortantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        // mise à jour des champs
        existing.setNumeroOrdre(dto.getNumeroOrdre());
        existing.setDateExpedition(dto.getDateExpedition());
        existing.setTypeDocument(dto.getTypeDocument());
        existing.setReference(dto.getReference());
        existing.setNature(dto.getNature());

        if (dto.getModeExpedition() != null)
            existing.setModeExpedition(ModeExpedition.valueOf(dto.getModeExpedition()));

        if (dto.getPriorite() != null)
            existing.setPriorite(Priorite.valueOf(dto.getPriorite()));

        
        if (dto.getDestinataireId() != null) {
            Tiers tiers = tiersRepository.findById(dto.getDestinataireId())
                    .orElseThrow(() -> new RuntimeException("Tiers introuvable"));
            existing.setDestinataire(tiers);
        }

        CourrierSortant updated = courrierSortantRepository.save(existing);

        return courrierMapper.toDTO(updated);
    }

    /**
     * 3. LISTE
     */
    public List<CourrierSortantDTO> getAll() {
        return courrierSortantRepository.findAll()
                .stream()
                .map(courrierMapper::toDTO)
                .collect(Collectors.toList()); // version ancienne
    }

    /**
     * 4. ARCHIVER
     */
    public CourrierSortantDTO archiver(Long id) {

        CourrierSortant entity = courrierSortantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        entity.setEtat(EtatCourrier.ARCHIVE);
        entity.setDateArchivage(LocalDateTime.now());

        CourrierSortant saved = courrierSortantRepository.save(entity);

        return courrierMapper.toDTO(saved);
    }
    // ARCHIVES
    public List<ArchiveSortantDTO> getArchives() {
        return courrierSortantRepository.findByEtat(EtatCourrier.ARCHIVE)
                .stream()
                .map(courrierMapper::toArchiveSortantDTO)
                .collect(Collectors.toList());
    }
    public void delete(Long id) {
        CourrierSortant entity = courrierSortantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        courrierSortantRepository.delete(entity);
    }
    // STATUT
    public CourrierSortantDTO changeStatut(Long id, String statut) {
        CourrierSortant entity = courrierSortantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier introuvable"));

        entity.setStatut(StatutCourrier.valueOf(statut.toUpperCase()));

        return courrierMapper.toDTO(courrierSortantRepository.save(entity));
    }
    public CourrierSortantDTO findById(Long id) {
        return courrierSortantRepository.findById(id)
                .map(courrierMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Introuvable"));
    }
    public CourrierSortant getEntityById(Long id) {
        return courrierSortantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courrier sortant introuvable"));
    }
}
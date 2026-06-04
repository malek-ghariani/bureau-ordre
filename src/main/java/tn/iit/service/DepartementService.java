package tn.iit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.DepartementDTO;
import tn.iit.entity.Departement;
import tn.iit.entity.Employe;
import tn.iit.mapper.DepartementMapper;
import tn.iit.repository.DepartementRepository;
import tn.iit.repository.EmployeRepository;

@Service
@RequiredArgsConstructor
public class DepartementService {

    private final DepartementRepository departementRepository;
    private final EmployeRepository employeRepository;
    private final DepartementMapper departementMapper;

    public List<DepartementDTO> findAll() {
        return departementRepository.findAll()
                .stream()
                .map(departementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DepartementDTO findById(String code) {
        return departementRepository.findById(code)
                .map(departementMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));
    }

    public DepartementDTO save(DepartementDTO dto) {

        Employe chef = null;

        if (dto.getChefDepartementId() != null) {
            chef = employeRepository.findById(dto.getChefDepartementId())
                    .orElseThrow(() -> new RuntimeException("Chef introuvable"));
        }

        Departement departement = departementMapper.toEntity(dto, chef);

        Departement saved = departementRepository.save(departement);

        return departementMapper.toDTO(saved);
    }

    public void deleteById(String code) {
        departementRepository.deleteById(code);
    }

    public boolean existsByCode(String code) {
        return departementRepository.existsById(code);
    }

    public List<DepartementDTO> findByNomContaining(String nom) {
        return departementRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(departementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DepartementDTO> findByChefDepartement(Long chefId) {
        return departementRepository.findByChefDepartementId(chefId)
                .stream()
                .map(departementMapper::toDTO)
                .collect(Collectors.toList());
    }
}
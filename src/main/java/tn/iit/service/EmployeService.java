package tn.iit.service;

import tn.iit.entity.Employe;
import tn.iit.entity.RoleEmploye;

import tn.iit.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    public Optional<Employe> findById(Long id) {
        return employeRepository.findById(id);
    }

    public Optional<Employe> findByEmail(String email) {
        return employeRepository.findByEmail(email);
    }

    public Optional<Employe> findByMatricule(String matricule) {
        return employeRepository.findByMatricule(matricule);
    }

    // ✅ Créer un nouvel employé — encode le mot de passe
    public Employe create(Employe employe) {
        employe.setPassword(passwordEncoder.encode(employe.getPassword()));
        return employeRepository.save(employe);
    }

    // ✅ Modifier un employé — ne touche pas au mot de passe
    public Employe update(Employe employe) {
        return employeRepository.save(employe);
    }

    // ✅ Modifier le mot de passe séparément
    public Employe updatePassword(Long id, String newPassword) {
        Employe employe = employeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employé introuvable"));
        employe.setPassword(passwordEncoder.encode(newPassword));
        return employeRepository.save(employe);
    }

    public void deleteById(Long id) {
        employeRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return employeRepository.existsByEmail(email);
    }

    public boolean existsByMatricule(String matricule) {
        return employeRepository.existsByMatricule(matricule);
    }

    public List<Employe> findByDepartement(String codeDepartement) {
        return employeRepository.findByDepartementCode(codeDepartement);
    }

    public List<Employe> findByRole(RoleEmploye role) {
        return employeRepository.findByRole(role);
    }

    public List<Employe> findByNomContaining(String nom) {
        return employeRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Employe> findActiveEmployes() {
        return employeRepository.findByEnabledTrue();
    }

    public Employe findByUsername(String username) {
        return employeRepository.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
    }
}
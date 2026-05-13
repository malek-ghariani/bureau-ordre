package tn.iit.service;

import tn.iit.entity.Employe;
import tn.iit.entity.Departement;
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
    private final PasswordEncoder passwordEncoder; // 🔹 important pour encoder le mot de passe

    // 🔹 Liste de tous les employés
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

    // 🔹 Sauvegarder un employé (encode le mot de passe si nouveau)
    public Employe save(Employe employe) {
        // Encoder le mot de passe uniquement si ce n'est pas déjà encodé
        if (employe.getPassword() != null) {
            employe.setPassword(passwordEncoder.encode(employe.getPassword()));
        }
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

    public List<Employe> findByRole(String role) {
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

    // 🔹 Optionnel : créer automatiquement l'admin au démarrage
    public void initAdmin(Departement departement) {
        if (!existsByEmail("admin@gmail.com")) {
            Employe admin = new Employe();
            admin.setMatricule("ADM001");
            admin.setNom("Administrateur");
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin123"); // sera encodé automatiquement
            admin.setPoste(tn.iit.entity.PosteEmploye.DIRECTEUR);
            admin.setRole(tn.iit.entity.RoleEmploye.ADMIN);
            admin.setDepartement(departement);
            admin.setEnabled(true);
            save(admin);
        }
    }
}

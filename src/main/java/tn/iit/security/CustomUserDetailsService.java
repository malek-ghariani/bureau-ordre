package tn.iit.security;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tn.iit.entity.Employe;
import tn.iit.repository.EmployeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(EmployeRepository employeRepository, PasswordEncoder passwordEncoder) {
        this.employeRepository = employeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(">>> Tentative de connexion : " + email);

        Employe employe = employeRepository.findByEmailAndEnabledTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        System.out.println(">>> Employé trouvé : " + employe.getNom() + " (" + employe.getRole() + ")");

        return User.builder()
                .username(employe.getEmail())
                .password(employe.getPassword()) // mot de passe déjà encodé en BD
                .roles(employe.getRole().name()) // ADMIN, RESPONSABLE, AGENT, USER
                .build();
    }
}

package tn.iit.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.iit.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import tn.iit.dto.LoginRequest;
import tn.iit.dto.LoginResponse;
import tn.iit.entity.Employe;
import tn.iit.security.JwtService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmployeRepository employeRepository;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Employe employe = employeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
            token,
            "Bearer",
            employe.getEmail(),
            employe.getRole().name(),
            employe.getNom(),
            employe.getMatricule(),
            employe.getId()
            
        );
    }

}

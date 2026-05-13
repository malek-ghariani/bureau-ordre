package tn.iit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utils")
public class PasswordTestController {

    @Autowired
    private PasswordEncoder encoder;

    // Générer le hash d’un mot de passe transmis en paramètre
    @GetMapping("/hash")
    public String hashPassword(@RequestParam String password) {
        return encoder.encode(password);
    }
}

package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.dto.LoginRequest;
import com.gestionchantier.backend.dto.LoginResponse;
import com.gestionchantier.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
    origins = "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-5173.app.github.dev"

)
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
public LoginResponse login(
        @RequestBody LoginRequest loginRequest
) {

return authService.login(
        loginRequest.getEmail(),
        loginRequest.getMotDePasse()
);
}
}
package dev.elder.ecommerce.controller;

import dev.elder.ecommerce.dto.request.CreateUser;
import dev.elder.ecommerce.dto.request.LoginRequest;
import dev.elder.ecommerce.dto.response.LoginResponse;
import dev.elder.ecommerce.entity.AuthUser;
import dev.elder.ecommerce.entity.Usuario;
import dev.elder.ecommerce.repository.UsuarioRepository;
import dev.elder.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok().body(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody CreateUser request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<Usuario>> findAll() {
        List<Usuario> users = authService.findAll();
        return ResponseEntity.ok().body(users);
    }

}

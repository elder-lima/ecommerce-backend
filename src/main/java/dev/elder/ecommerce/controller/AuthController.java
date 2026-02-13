package dev.elder.ecommerce.controller;

import dev.elder.ecommerce.dto.request.CreateUser;
import dev.elder.ecommerce.dto.request.LoginRequest;
import dev.elder.ecommerce.dto.response.LoginResponse;
import dev.elder.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@Tag(name = "auth", description = "Autenticação e registro")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Método para login de usuário")
    @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso")
    @ApiResponse(responseCode = "401", description = "Email ou Senha Inválidos")
    @ApiResponse(responseCode = "500", description = "Erro no Servidor")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok().body(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Registro de usuario", description = "Método para salvar um usuário")
    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso")
    @ApiResponse(responseCode = "409", description = "Email ou CPF já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro no Servidor")
    public ResponseEntity<Void> register(@RequestBody @Valid CreateUser request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}

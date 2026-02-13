package dev.elder.ecommerce.controller;

import dev.elder.ecommerce.dto.request.UsuarioUpdateRequest;
import dev.elder.ecommerce.dto.response.UsuarioDetailResponse;

import dev.elder.ecommerce.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "usuarios", description = "Operações relacionadas aos usuários")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    @Operation(summary = "Buscar usuário logado")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<UsuarioDetailResponse> me(JwtAuthenticationToken token) {

        UUID usuarioIdFromToken = UUID.fromString(token.getName());

        return ResponseEntity.ok(usuarioService.findById(usuarioIdFromToken));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Buscar usuário por ID (Apenas ADMIN)")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<UsuarioDetailResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(usuarioService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Listar todos os usuários (Apenas ADMIN)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<List<UsuarioDetailResponse>> findAll() {
        return ResponseEntity.ok().body(usuarioService.findAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Atualizar usuário por Id (Apenas ADMIN)")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<UsuarioDetailResponse> update(@PathVariable(name = "id") UUID id, @RequestBody @Valid UsuarioUpdateRequest dto) {
        return ResponseEntity.ok().body(usuarioService.update(id, dto));
    }

    @PutMapping("/me")
    @Operation(summary = "Atualizar usuário logado")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado")
    public ResponseEntity<UsuarioDetailResponse> updateMe(@RequestBody @Valid UsuarioUpdateRequest dto, JwtAuthenticationToken token) {

        UUID usuarioId = UUID.fromString(token.getName());

        return ResponseEntity.ok(usuarioService.update(usuarioId, dto));
    }

}

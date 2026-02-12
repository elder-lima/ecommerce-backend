package dev.elder.ecommerce.controller;

import dev.elder.ecommerce.dto.request.UsuarioUpdateRequest;
import dev.elder.ecommerce.dto.response.UsuarioDetailResponse;
import dev.elder.ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetailResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(usuarioService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<UsuarioDetailResponse>> findAll() {
        return ResponseEntity.ok().body(usuarioService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDetailResponse> update(@PathVariable(name = "id") UUID id, @RequestBody @Valid UsuarioUpdateRequest dto) {
        return ResponseEntity.ok().body(usuarioService.update(id, dto));
    }

}

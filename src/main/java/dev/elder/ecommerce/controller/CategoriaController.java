package dev.elder.ecommerce.controller;

import dev.elder.ecommerce.dto.request.CategoriaRequest;
import dev.elder.ecommerce.dto.response.CategoriaResponse;
import dev.elder.ecommerce.entity.Categoria;
import dev.elder.ecommerce.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categoria")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{nome}")
    public ResponseEntity<CategoriaResponse> findByNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.findByName(nome));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<CategoriaResponse> insert(@RequestBody @Valid CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(request));
    }

    @DeleteMapping("/{nome}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String nome) {
        service.delete(nome);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

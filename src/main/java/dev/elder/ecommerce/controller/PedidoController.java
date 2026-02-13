package dev.elder.ecommerce.controller;

import dev.elder.ecommerce.dto.request.PedidoRequest;
import dev.elder.ecommerce.dto.response.PedidoResponse;
import dev.elder.ecommerce.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
@Tag(name = "pedidos", description = "Operações de pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar pedidos do usuário logado")
    @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<List<PedidoResponse>> findAllPedidosByUsuario(JwtAuthenticationToken token) {
        return ResponseEntity.ok().body(service.findByUsuario(token));
    }

    @PostMapping
    @Operation(summary = "Criar pedido")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<PedidoResponse> criarPedido(@RequestBody @Valid PedidoRequest pedidoRequest, JwtAuthenticationToken token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarPedido(pedidoRequest, token));
    }

}

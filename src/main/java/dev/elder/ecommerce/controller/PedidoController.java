package dev.elder.ecommerce.controller;

import dev.elder.ecommerce.dto.request.PedidoRequest;
import dev.elder.ecommerce.dto.response.PedidoResponse;
import dev.elder.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> findByUsuario(JwtAuthenticationToken token) {
        return ResponseEntity.ok().body(service.findByUsuario(token));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@RequestBody @Valid PedidoRequest pedidoRequest, JwtAuthenticationToken token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarPedido(pedidoRequest, token));
    }

}

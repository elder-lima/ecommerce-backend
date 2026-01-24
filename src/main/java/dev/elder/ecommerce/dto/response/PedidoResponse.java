package dev.elder.ecommerce.dto.response;

import dev.elder.ecommerce.entity.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(

        UUID pedidoId,
        Instant criacao,
        StatusPedido status,
        BigDecimal total,
        UsuarioResponse usuarioResponse,
        List<PedidoItemResponse> itens

) {}

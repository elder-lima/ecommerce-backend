package dev.elder.ecommerce.dto.request;

import dev.elder.ecommerce.entity.Produto;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public record PedidoRequest(

        @NotNull
        List<PedidoItemRequest> itens

) {}

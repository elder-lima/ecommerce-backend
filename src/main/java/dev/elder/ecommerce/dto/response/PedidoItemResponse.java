package dev.elder.ecommerce.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoItemResponse(

        UUID idProduto,
        String nomeProduto,
        Integer quantidade,
        BigDecimal preco

) {}

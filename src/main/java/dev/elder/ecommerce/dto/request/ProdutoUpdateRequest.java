package dev.elder.ecommerce.dto.request;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public record ProdutoUpdateRequest(
        String nome,
        String descricao,
        @Positive
        BigDecimal preco,
        String imagem,
        Set<CategoriaUpdateRequest> categorias
) {
}

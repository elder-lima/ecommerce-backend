package dev.elder.ecommerce.dto.response;

import java.math.BigDecimal;

import java.util.Set;
import java.util.UUID;

public record ProdutoResponse(

        UUID id,
        String nome,
        String descricao,
        BigDecimal preco,
        String imagem,
        Set<CategoriaResponse> categorias

) {}

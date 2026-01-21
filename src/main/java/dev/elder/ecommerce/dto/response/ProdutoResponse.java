package dev.elder.ecommerce.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProdutoResponse(

        UUID id,
        String nome,
        String descricao,
        BigDecimal preco,
        String imagem,
        List<CategoriaResponse> categorias

) {}

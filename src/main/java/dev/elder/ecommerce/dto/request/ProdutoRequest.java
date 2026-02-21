package dev.elder.ecommerce.dto.request;

import dev.elder.ecommerce.entity.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public record ProdutoRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatório")
        String descricao,

        @NotNull(message = "Preço é obrigatório")
        @Positive
        BigDecimal preco,

        @NotBlank(message = "imgem é obrigatório")
        String imagem,

        @NotEmpty
        Set<Categoria> categorias

) {}

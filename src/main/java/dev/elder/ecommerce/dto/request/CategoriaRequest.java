package dev.elder.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(

        @NotBlank(message = "Nome da Categoria é obrigatório")
        String nome
) {}

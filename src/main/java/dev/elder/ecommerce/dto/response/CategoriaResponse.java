package dev.elder.ecommerce.dto.response;

import java.util.UUID;

public record CategoriaResponse (
        UUID id,
        String nome
){}

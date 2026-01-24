package dev.elder.ecommerce.dto.response;

import java.util.UUID;

public record UsuarioResponse(

        UUID id,
        String email,
        String nome,
        String telefone

) {}

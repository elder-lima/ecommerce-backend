package dev.elder.ecommerce.dto.response;

import java.util.UUID;

public record UsuarioDetailResponse(

        UUID id,
        String email,
        String nome,
        String telefone,
        String cpf

) {
}

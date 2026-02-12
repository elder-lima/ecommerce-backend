package dev.elder.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioUpdateRequest(

        String nome,
        String telefone,
        @CPF(message = "CPF inválido")
        String cpf,
        @Email(message = "Email válido é Obrigatório.")
        String email,
        String senha
) {
}

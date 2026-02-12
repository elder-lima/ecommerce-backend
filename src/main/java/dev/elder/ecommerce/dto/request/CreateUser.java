package dev.elder.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record CreateUser(

        @NotBlank(message = "Email é obrigatório")
        @Email
        @Size(max = 100)
        String email,

        @NotBlank(message = "Senha é obrigatório")
        String senha,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String nome,

        @Size(max = 15)
        String telefone,

        @NotBlank(message = "CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String cpf

) {}

package dev.elder.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Email é obrigatório")
        @Email
        @Size(max = 100)
        String email,

        @NotBlank(message = "Senha é obrigatória.")
        String senha

){}

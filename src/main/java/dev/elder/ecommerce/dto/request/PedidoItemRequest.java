package dev.elder.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PedidoItemRequest(

        @NotNull
        UUID produtoId,

        @NotNull
        @Min(1)
        Integer quantidade

) {}

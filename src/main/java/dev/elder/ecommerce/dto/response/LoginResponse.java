package dev.elder.ecommerce.dto.response;

public record LoginResponse(

        String accessToken,
        Long expiresIn

) {}

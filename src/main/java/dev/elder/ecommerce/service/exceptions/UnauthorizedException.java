package dev.elder.ecommerce.service.exceptions;

public class UnauthorizedException  extends RuntimeException{
    public UnauthorizedException(String msg) {
        super(msg);
    }
}

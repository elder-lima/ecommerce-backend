package dev.elder.ecommerce.controller.exceptions;

import dev.elder.ecommerce.service.exceptions.ConflictException;
import dev.elder.ecommerce.service.exceptions.DatabaseException;
import dev.elder.ecommerce.service.exceptions.ResourceNotFoundException;
import dev.elder.ecommerce.service.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

// Tratador Global de exceções, qualquer exceção lançada em qualquer controller da aplicação pode ser interceptada aqui.
@ControllerAdvice
public class ResourceExceptionHandler {

    // Recurso não encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Recurso não encontrado."; // Mensagem padrão para o cliente
        HttpStatus status = HttpStatus.NOT_FOUND; // Status HTTP 404
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI()); // estrutura do erro
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
        String error = "Erro no Banco de Dados.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // Tratando erros de validação (@Valid) – 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {

        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Validation error");
        err.setMessage("Invalid fields");
        err.setPath(request.getRequestURI());

        // Percorre todos os campos inválidos, captura e adiciona na resposta de erro, no campo errors[]
        for (FieldError f : e.getBindingResult() //relatório completo da validação
                .getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // Tratando ConflictException (409)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> conflict(
            ConflictException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT; // Retorna 409, semanticamente correto para duplicidade.

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Conflict",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Tratando erro de Autorização (401). Ex: erro no login
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> handleUnauthorized(UnauthorizedException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Unauthorized",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }

}
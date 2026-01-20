package dev.elder.ecommerce.controller.exceptions;

import java.util.ArrayList;
import java.util.List;

// ValidationError herda os campos de StandardError
public class ValidationError extends StandardError {

    // Uma lista de erros específicos por campo:
    // Nome do campo inválido e Mensagem de erro daquele campo
    private List<FieldMessage> errors = new ArrayList<>(); // Sem essa lista, o cliente só saberia que a requisição é inválida, mas não saberia o porquê.

    public void addError(String field, String message) {
        errors.add(new FieldMessage(field, message));
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

}

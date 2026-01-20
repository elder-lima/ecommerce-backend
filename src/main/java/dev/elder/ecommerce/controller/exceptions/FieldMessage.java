package dev.elder.ecommerce.controller.exceptions;

public class FieldMessage {

    // Esses valores vêm diretamente das anotações de validação como @NotBlank, @Size, @Notnull ...
    private String field; // nome do atributo inválido
    private String message; // mensagem de validação

    // Esse construtor garante que o objeto já nasce com dados válidos
    public FieldMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

    // Jackson usa os getters para serializar o JSON
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    // Jackson usa os getters para serializar o JSON
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

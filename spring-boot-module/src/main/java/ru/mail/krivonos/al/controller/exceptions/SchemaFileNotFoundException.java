package ru.mail.krivonos.al.controller.exceptions;

public class SchemaFileNotFoundException extends RuntimeException {

    public SchemaFileNotFoundException(String message) {
        super(message);
    }
}

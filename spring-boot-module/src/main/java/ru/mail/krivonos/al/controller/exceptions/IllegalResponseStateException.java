package ru.mail.krivonos.al.controller.exceptions;

public class IllegalResponseStateException extends RuntimeException {

    public IllegalResponseStateException(String message) {
        super(message);
    }
}

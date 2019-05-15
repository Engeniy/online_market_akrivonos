package ru.mail.krivonos.al.service.exceptions;

public class ConnectionAutoCloseException extends RuntimeException {

    public ConnectionAutoCloseException(String message, Exception e) {
        super(message, e);
    }
}

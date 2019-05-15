package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message, Exception e) {
        super(message, e);
    }
}

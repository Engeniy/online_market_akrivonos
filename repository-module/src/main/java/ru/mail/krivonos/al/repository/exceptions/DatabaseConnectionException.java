package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String s, Exception e) {
        super(s, e);
    }
}

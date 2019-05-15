package ru.mail.krivonos.al.repository.exceptions;

public class UserRepositoryException extends RuntimeException {

    public UserRepositoryException(String message, Exception e) {
        super(message, e);
    }
}

package ru.mail.krivonos.al.repository.exceptions;

public class RoleRepositoryException extends RuntimeException {

    public RoleRepositoryException(String message, Exception e) {
        super(message, e);
    }
}

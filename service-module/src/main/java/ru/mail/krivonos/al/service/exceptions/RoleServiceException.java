package ru.mail.krivonos.al.service.exceptions;

public class RoleServiceException extends RuntimeException {

    public RoleServiceException(String message, Exception e) {
        super(message, e);
    }
}

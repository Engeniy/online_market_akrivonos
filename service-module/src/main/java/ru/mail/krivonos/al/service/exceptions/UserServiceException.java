package ru.mail.krivonos.al.service.exceptions;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message, Exception e) {
        super(message, e);
    }
}

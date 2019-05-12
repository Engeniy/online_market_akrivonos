package ru.mail.krivonos.al.service.exceptions;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String s, Exception e) {
        super(s, e);
    }
}

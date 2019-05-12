package ru.mail.krivonos.al.service.exceptions;

public class RoleServiceException extends RuntimeException {

    public RoleServiceException(String s, Exception e) {
        super(s, e);
    }
}

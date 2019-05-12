package ru.mail.krivonos.al.repository.exceptions;

public class RoleRepositoryException extends RuntimeException {

    public RoleRepositoryException(String s, Exception e) {
        super(s, e);
    }
}

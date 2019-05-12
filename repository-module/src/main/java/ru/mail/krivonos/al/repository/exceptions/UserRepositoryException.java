package ru.mail.krivonos.al.repository.exceptions;

public class UserRepositoryException extends RuntimeException {

    public UserRepositoryException(String s, Exception e) {
        super(s, e);
    }
}

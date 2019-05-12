package ru.mail.krivonos.al.repository.exceptions;

public class ReviewRepositoryException extends RuntimeException {

    public ReviewRepositoryException(String s, Exception e) {
        super(s, e);
    }
}

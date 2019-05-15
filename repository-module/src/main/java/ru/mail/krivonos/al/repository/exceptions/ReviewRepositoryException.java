package ru.mail.krivonos.al.repository.exceptions;

public class ReviewRepositoryException extends RuntimeException {

    public ReviewRepositoryException(String message, Exception e) {
        super(message, e);
    }
}

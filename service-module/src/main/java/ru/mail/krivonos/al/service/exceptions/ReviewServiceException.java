package ru.mail.krivonos.al.service.exceptions;

public class ReviewServiceException extends RuntimeException {

    public ReviewServiceException(String message, Exception e) {
        super(message, e);
    }
}

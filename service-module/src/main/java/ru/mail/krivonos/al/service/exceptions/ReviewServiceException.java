package ru.mail.krivonos.al.service.exceptions;

public class ReviewServiceException extends RuntimeException {

    public ReviewServiceException(String s, Exception e) {
        super(s, e);
    }
}

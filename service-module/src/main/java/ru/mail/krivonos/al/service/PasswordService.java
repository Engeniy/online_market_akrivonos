package ru.mail.krivonos.al.service;

public interface PasswordService {

    String getPassword(String email);

    String encodePassword(String password);

    boolean matches(String encodedPassword, String password);
}

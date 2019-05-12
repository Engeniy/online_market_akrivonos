package ru.mail.krivonos.al.service.generator.impl;

import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.service.generator.PasswordGenerator;

import java.util.Random;

@Component("passwordGenerator")
public class PasswordGeneratorImpl implements PasswordGenerator {

    private static final String SYMBOLS_STRING = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private final Random random = new Random();

    @Override
    public String getPassword() {
        int passwordLength = random.nextInt(MAX_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH + 1) + MIN_PASSWORD_LENGTH;
        String[] symbolsArray = SYMBOLS_STRING.split("");
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            password.append(symbolsArray[random.nextInt(symbolsArray.length)]);
        }
        return password.toString();
    }
}

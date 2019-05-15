package ru.mail.krivonos.al.service.generator;

import org.junit.Test;
import ru.mail.krivonos.al.service.generator.impl.PasswordGeneratorImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PasswordGeneratorTest {

    private PasswordGenerator passwordGenerator = new PasswordGeneratorImpl();

    @Test
    public void shouldReturnNotNullPassword() {
        String password = passwordGenerator.getPassword();
        assertNotNull(password);
    }

    @Test
    public void shouldReturnNotEmptyPassword() {
        String password = passwordGenerator.getPassword();
        assertFalse(password.isEmpty());
    }

    @Test
    public void shouldReturnPasswordWithFourSymbolsOrLonger() {
        String password = passwordGenerator.getPassword();
        assertTrue(password.length() >= 4);
    }

    @Test
    public void shouldReturnPasswordNotLongerThenTwentySymbols() {
        String password = passwordGenerator.getPassword();
        assertTrue(password.length() <= 20);
    }
}

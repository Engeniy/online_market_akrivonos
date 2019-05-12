package ru.mail.krivonos.al.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mail.krivonos.al.service.generator.PasswordGenerator;
import ru.mail.krivonos.al.service.impl.PasswordServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordGenerator passwordGenerator;
    @Mock
    private MailService mailService;

    private PasswordService passwordService;

    @Before
    public void init() {
        passwordService = new PasswordServiceImpl(passwordEncoder, passwordGenerator, mailService);
    }

    @Test
    public void shouldReturnEncodedPassword() {
        String password = "password";
        String encodedPassword = "encoded password";
        when(passwordGenerator.getPassword()).thenReturn(password);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        String result = passwordService.getPassword("email");
        assertEquals(encodedPassword, result);
    }
}

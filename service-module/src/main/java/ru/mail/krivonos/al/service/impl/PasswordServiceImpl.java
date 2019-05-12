package ru.mail.krivonos.al.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.service.MailService;
import ru.mail.krivonos.al.service.PasswordService;
import ru.mail.krivonos.al.service.generator.PasswordGenerator;

@Service("passwordService")
public class PasswordServiceImpl implements PasswordService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final MailService mailService;

    @Autowired
    public PasswordServiceImpl(
            PasswordEncoder passwordEncoder,
            PasswordGenerator passwordGenerator,
            MailService mailService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.passwordGenerator = passwordGenerator;
        this.mailService = mailService;
    }

    @Override
    public String getPassword(String email) {
        String password = passwordGenerator.getPassword();
        mailService.sendMessage(email, password);
        return passwordEncoder.encode(password);
    }
}

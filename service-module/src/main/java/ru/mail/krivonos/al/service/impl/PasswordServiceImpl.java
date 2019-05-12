package ru.mail.krivonos.al.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.service.PasswordService;
import ru.mail.krivonos.al.service.generator.PasswordGenerator;

@Service("passwordService")
public class PasswordServiceImpl implements PasswordService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;

    @Autowired
    public PasswordServiceImpl(PasswordEncoder passwordEncoder, PasswordGenerator passwordGenerator) {
        this.passwordEncoder = passwordEncoder;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public String getPassword() {
        String password = passwordGenerator.getPassword();
        logger.info("Generated password: {}", password);
        return passwordEncoder.encode(password);
    }
}

package ru.mail.krivonos.al.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.mail.krivonos.al.service.properties.MailProperties;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    private static final String TRANSPORT_PROTOCOL_KEY = "mail.transport.protocol";
    private static final String SMTP_AUTH_KEY = "mail.smtp.auth";
    private static final String SMTP_STARTTLS_ENABLE_KEY = "mail.smtp.starttls.enable";
    private static final String MAIL_DEBUG_KEY = "mail.debug";

    private final MailProperties mailProperties;

    @Autowired
    public MailConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties.getMailHost());
        javaMailSender.setPort(mailProperties.getMailPort());
        javaMailSender.setUsername(mailProperties.getMailUsername());
        javaMailSender.setPassword(mailProperties.getMailPassword());

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put(TRANSPORT_PROTOCOL_KEY, mailProperties.getMailTransportProtocol());
        properties.put(SMTP_AUTH_KEY, mailProperties.getMailSMTPAuth());
        properties.put(SMTP_STARTTLS_ENABLE_KEY, mailProperties.getMailSMTPStartTLSEnable());
        properties.put(MAIL_DEBUG_KEY, mailProperties.getMailDebug());
        return javaMailSender;
    }
}

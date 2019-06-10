package ru.mail.krivonos.al.service.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mailProperties")
public class MailProperties {

    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.port}")
    private int mailPort;
    @Value("${mail.username}")
    private String mailUsername;
    @Value("${mail.password}")
    private String mailPassword;
    @Value("${mail.transport.protocol}")
    private String mailTransportProtocol;
    @Value("${mail.smtp.auth}")
    private String mailSMTPAuth;
    @Value("${mail.smtp.starttls.enable}")
    private String mailSMTPStartTLSEnable;
    @Value("${mail.debug}")
    private String mailDebug;

    public String getMailHost() {
        return mailHost;
    }

    public int getMailPort() {
        return mailPort;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public String getMailTransportProtocol() {
        return mailTransportProtocol;
    }

    public String getMailSMTPAuth() {
        return mailSMTPAuth;
    }

    public String getMailSMTPStartTLSEnable() {
        return mailSMTPStartTLSEnable;
    }

    public String getMailDebug() {
        return mailDebug;
    }
}

package com.mpedroni.aprendatech.infra.providers;

import com.mpedroni.aprendatech.domain.providers.EmailSender;
import org.springframework.stereotype.Service;

@Service
public class FakeEmailSender implements EmailSender {
    @Override
    public void send(String recipient, String subject, String body) {
        System.out.printf("Simulating sending email to [%s]:\n%n", recipient);

        System.out.printf("""
                Subject: %s
                Body: %s
            %n""", subject, body);
    }
}

package com.mpedroni.aprendatech.domain.providers;

public interface EmailSender {
    void send(String recipient, String subject, String content);
}

package jhrspring.splearn.adapter.integration;

import jhrspring.splearn.application.required.EmailSender;
import jhrspring.splearn.domain.Email;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender email :" + email);
    }
}

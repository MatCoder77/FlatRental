package com.flatrental.domain.authentication;

import com.flatrental.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConfirmationEventListener implements ApplicationListener<RegistrationConfirmationEvent> {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AuthenticationService authenticationService;

    private static final String CONFIRMATION_EMAIL_SUBJECT = "Flat Renal - Registration Confirmation";

    @Override
    public void onApplicationEvent(RegistrationConfirmationEvent registrationConfirmationEvent) {
        sendConfirmationEmail(registrationConfirmationEvent);
    }

    private void sendConfirmationEmail(RegistrationConfirmationEvent event) {
        User user = event.getUser();
        VerificationToken verificationToken = authenticationService.createVerificationToken(user);
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(CONFIRMATION_EMAIL_SUBJECT);
        email.setText("Activation link: " + "<a>https://localhost:3000/confirm-registration?token=" + verificationToken.getToken() + "</a>");
        javaMailSender.send(email);
    }

}

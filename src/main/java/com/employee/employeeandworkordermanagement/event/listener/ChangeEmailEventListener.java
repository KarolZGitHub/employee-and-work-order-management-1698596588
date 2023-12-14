package com.employee.employeeandworkordermanagement.event.listener;

import com.employee.employeeandworkordermanagement.event.ChangeEmailEvent;
import com.employee.employeeandworkordermanagement.service.ChangeEmailResetTokenService;
import com.employee.employeeandworkordermanagement.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChangeEmailEventListener implements ApplicationListener<ChangeEmailEvent> {
    private final JavaMailSender mailSender;
    private final ChangeEmailResetTokenService changeEmailResetTokenService;
    private User theUser;


    public void onApplicationEvent(ChangeEmailEvent event) {
        // 1. Get the newly registered user
        theUser = event.getUser();
        //2. Create a verification token for the user
        String changeEmailToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        changeEmailResetTokenService.createEmailChangeTokenForUser(theUser, changeEmailToken);
        //4 Build the verification url to be sent to the user
        String url = event.getApplicationUrl() + "/profile/email-change-request-process?token=" + changeEmailToken;
        //5. Send the email.
        try {
            sendEmailChangeVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailChangeVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Change Request Verification";
        String senderName = "Employee Manager e-mail manager";
        String mailContent = "<p> Hi, " + theUser.getFirstName() + ", </p>" +
                "<p><b>You recently requested to change your E-mail,</b>" + "" +
                "Please, follow the link below to complete the action.</p>" +
                "<a href=\"" + url + "\">Change E-mail</a>" +
                "<p> Thank you <br> Employee Manager.";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("javadeveloper2509@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}

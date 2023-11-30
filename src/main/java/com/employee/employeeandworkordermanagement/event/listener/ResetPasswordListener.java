package com.employee.employeeandworkordermanagement.event.listener;

import com.employee.employeeandworkordermanagement.event.ResetPasswordEvent;
import com.employee.employeeandworkordermanagement.password.PasswordResetTokenRepository;
import com.employee.employeeandworkordermanagement.password.PasswordResetTokenService;
import com.employee.employeeandworkordermanagement.user.User;
import com.employee.employeeandworkordermanagement.user.UserService;
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
public class ResetPasswordListener implements ApplicationListener<ResetPasswordEvent> {
    private final UserService userService;

    private final JavaMailSender mailSender;
    private User theUser;


    public void onApplicationEvent(ResetPasswordEvent event) {
        // 1. Get the newly registered user
        theUser = event.getUser();
        //2. Create a verification token for the user
        String resetPasswordToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.createPasswordResetTokenForUser(theUser,resetPasswordToken);
        //4 Build the verification url to be sent to the user
        String url = event.getApplicationUrl() + "/register/reset-password?token=" + resetPasswordToken;
        //5. Send the email.
        try {
            sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPasswordResetVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request Verification";
        String senderName = "Employee Manager password manager";
        String mailContent = "<p> Hi, " + theUser.getFirstName() + ", </p>" +
                "<p><b>You recently requested to reset your password,</b>" + "" +
                "Please, follow the link below to complete the action.</p>" +
                "<a href=\"" + url + "\">Reset password</a>" +
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

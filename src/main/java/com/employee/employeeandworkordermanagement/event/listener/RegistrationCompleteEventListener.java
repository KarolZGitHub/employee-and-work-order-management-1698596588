package com.employee.employeeandworkordermanagement.event.listener;

import com.employee.employeeandworkordermanagement.event.RegistrationCompleteEvent;
import com.employee.employeeandworkordermanagement.user.User;
import com.employee.employeeandworkordermanagement.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Get user from event
        User theUser = event.getUser();
        //Create verification token
        String verificationToken = UUID.randomUUID().toString();
        //Save token
        userService.saveUserVerificationToken(theUser, verificationToken);
        //Build verification token for user
        String url = event.getApplicationUrl() + "register/verifyEmail?token" + verificationToken;

    }
}

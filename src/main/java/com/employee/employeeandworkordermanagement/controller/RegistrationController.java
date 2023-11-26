package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.Registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.Registration.token.VerificationToken;
import com.employee.employeeandworkordermanagement.Registration.token.VerificationTokenService;
import com.employee.employeeandworkordermanagement.event.RegistrationCompleteEvent;
import com.employee.employeeandworkordermanagement.user.User;
import com.employee.employeeandworkordermanagement.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/user")
    public String registerUserHandle(RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "successRegister";
    }

    @GetMapping("/verifyEmail")
    public String verifyToken(@RequestParam("token") String token) {
        VerificationToken theToken = verificationTokenService.getToken(token);
        if (theToken.getUser().isEnabled()) {
            return "token/tokenVerified";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("token expired")) {
            return "token/tokenExpired";
        }
        if (verificationResult.equalsIgnoreCase("valid")) {
            return "email/emailVerified";
        }
        return "token/tokenDoesntExist";
    }

    @GetMapping("/user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
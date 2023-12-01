package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.Registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.event.RegistrationCompleteEvent;
import com.employee.employeeandworkordermanagement.user.User;
import com.employee.employeeandworkordermanagement.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping
    public String registerUser(RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "successRegister";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName()+":"+ request.getServerPort()+request.getContextPath();
    }

}
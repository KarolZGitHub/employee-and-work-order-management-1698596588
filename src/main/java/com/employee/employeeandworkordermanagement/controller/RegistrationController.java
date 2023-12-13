package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.registration.token.VerificationToken;
import com.employee.employeeandworkordermanagement.service.VerificationTokenService;
import com.employee.employeeandworkordermanagement.event.RegistrationCompleteEvent;
import com.employee.employeeandworkordermanagement.event.ResetPasswordEvent;
import com.employee.employeeandworkordermanagement.password.PasswordResetProcess;
import com.employee.employeeandworkordermanagement.password.PasswordResetRequest;
import com.employee.employeeandworkordermanagement.password.PasswordResetToken;
import com.employee.employeeandworkordermanagement.service.PasswordResetTokenService;
import com.employee.employeeandworkordermanagement.user.User;
import com.employee.employeeandworkordermanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService verificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/user")
    public String registerUserHandle(RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "/email/successRegister";
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

    @GetMapping("/password-reset-request")
    public String showResetPasswordForm(Model model, Authentication authentication) {
        if(authentication.isAuthenticated()){
            UserDTO userDTO = userService.convertUserToUserDTO(userService.findByEmail(authentication.getName()).get());
            model.addAttribute("user",userDTO);
        }
        model.addAttribute("passwordResetRequest", new PasswordResetRequest());
        return "/password/resetPasswordRequest";
    }

    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(PasswordResetRequest passwordResetRequest,
                                       final HttpServletRequest request) {
        Optional<User> user = userService.findByEmail(passwordResetRequest.getEmail());
        if (user.isPresent()) {
            publisher.publishEvent(new ResetPasswordEvent(user.get(), applicationUrl(request)));
            return "password/passwordTokenSent";
        }
        return "password/wrongEmail";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token) {
        if (passwordResetTokenService.validatePasswordResetToken(token).equalsIgnoreCase("token expired")) {
            return "/token/tokenExpired";
        }
        if (passwordResetTokenService.validatePasswordResetToken(token).equalsIgnoreCase("valid")) {
            return "redirect:/register/change-password-form?token=" + token;
        }
        return "token/tokenDoesntExist";
    }

    @GetMapping("/change-password-form")
    public String showChangePasswordForm(@RequestParam("token") String token, Model model) {
        if (passwordResetTokenService.validatePasswordResetToken(token).equalsIgnoreCase("token expired")) {
            return "/token/tokenExpired";
        }
        if (passwordResetTokenService.validatePasswordResetToken(token).equalsIgnoreCase("valid")) {
            model.addAttribute("passwordResetProcess", new PasswordResetProcess());
            model.addAttribute("token", token);
            return "/password/changePasswordForm";
        }
        return "token/tokenDoesntExist";

    }

    @PostMapping("/change-password-form")
    public String changePasswordProcess(@RequestParam("token") String token, PasswordResetProcess passwordResetProcess) {
        if (!passwordResetProcess.getRepeatPassword().equalsIgnoreCase(passwordResetProcess.getPassword())) {
            return "/password/passwordsAreNotEqual";
        }
        if (passwordResetTokenService.validatePasswordResetToken(token).equalsIgnoreCase("token expired")) {
            return "/token/tokenExpired";
        }
        if (passwordResetTokenService.validatePasswordResetToken(token).equalsIgnoreCase("valid")) {
            PasswordResetToken passwordResetToken = passwordResetTokenService.getToken(token);
            User user = passwordResetToken.getUser();
            userService.saveUser(user, passwordResetProcess.getPassword(), passwordResetProcess.getRepeatPassword());
            return "password/passwordChanged";
        }
        return "token/tokenDoesntExist";

    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
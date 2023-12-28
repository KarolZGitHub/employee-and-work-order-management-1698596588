package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.event.ChangeEmailEvent;
import com.employee.employeeandworkordermanagement.password.PasswordResetProcess;
import com.employee.employeeandworkordermanagement.profile.ChangeEmailRequest;
import com.employee.employeeandworkordermanagement.profile.ChangeFirstOrLastName;
import com.employee.employeeandworkordermanagement.service.ChangeEmailResetTokenService;
import com.employee.employeeandworkordermanagement.service.UserService;
import com.employee.employeeandworkordermanagement.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class MyProfileController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final ChangeEmailResetTokenService changeEmailResetTokenService;

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }

    @GetMapping("/my-profile")
    public String showMyProfile() {
        return "myProfile/myProfile";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model, PasswordResetProcess passwordResetProcess) {
        model.addAttribute("passwordResetProcess", passwordResetProcess);
        return "myProfile/changePassword";
    }

    @PostMapping("/change-password")
    public String handleChangePassword(Authentication authentication, PasswordResetProcess passwordResetProcess,
                                       Model model) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        boolean changePasswordSuccess = userService.changePassword(user, passwordResetProcess.getPassword()
                , passwordResetProcess.getRepeatPassword());
        if (changePasswordSuccess) {
            return "redirect:/profile/my-profile";
        } else {
            model.addAttribute("error", "Passwords are not the same.");
            return "myProfile/error";
        }
    }

    @GetMapping("/change-first-name")
    public String showFirstNameChangeForm(Model model, ChangeFirstOrLastName changeFirstOrLastName) {
        model.addAttribute("changeFirstOrLastName", changeFirstOrLastName);
        return "myProfile/changeFirstName";
    }

    @PostMapping("/change-first-name")
    public String handleFirstNameChange(ChangeFirstOrLastName changeFirstOrLastName, Authentication authentication,
                                        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        user.setFirstName(changeFirstOrLastName.getFirstOrLastName());
        userService.changeFirstName(user, changeFirstOrLastName.getFirstOrLastName());
        return "redirect:/profile/my-profile";
    }

    @GetMapping("/change-last-name")
    public String showLastNameChangeForm(Model model, ChangeFirstOrLastName changeFirstOrLastName) {
        model.addAttribute("changeFirstOrLastName", changeFirstOrLastName);
        return "myProfile/changeLastName";
    }

    @PostMapping("/change-last-name")
    public String handleLastNameChange(ChangeFirstOrLastName changeFirstOrLastName, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        user.setLastName(changeFirstOrLastName.getFirstOrLastName());
        userService.changeLastName(user, changeFirstOrLastName.getFirstOrLastName());
        return "redirect:/profile/my-profile";
    }

    @GetMapping("/email-change-request")
    public String showEmailChangeForm(Authentication authentication, final HttpServletRequest request) {
        User user = userService.findByEmail(authentication.getName()).get();
        publisher.publishEvent(new ChangeEmailEvent(user, applicationUrl(request)));
        return "myProfile/emailTokenSent";
    }

    @GetMapping("/email-change-request-process")
    public String verifyChangeEmailToken(@RequestParam("token") String token,
                                         Model model,
                                         ChangeEmailRequest changeEmailRequest
    ) {
        String verificationResult = changeEmailResetTokenService.validatePasswordResetToken(token);
        if (verificationResult.equalsIgnoreCase("token expired")) {
            return "token/tokenExpired";
        }
        if (verificationResult.equalsIgnoreCase("valid")) {
            model.addAttribute("changeEmailRequest", changeEmailRequest);
            return "myProfile/changeEmailForm";
        }
        return "token/tokenDoesntExist";
    }

    @PostMapping("/email-change-request-process")
    public String handleEmailChangeProcess(Authentication authentication,
                                           ChangeEmailRequest changeEmailRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        Optional<User> userOptional = userService.findByEmail(changeEmailRequest.getEmail());
        if (userOptional.isPresent()) {
            return "error/emailTaken";
        }
        if (changeEmailRequest.getEmail().equals(changeEmailRequest.getConfirmEmail())) {
            User user = userService.findByEmail(authentication.getName()).get();
            userService.saveEmailForUser(user, changeEmailRequest.getEmail());
            return "redirect:/logout";
        } else {
            return "myProfile/emailDoesntMatch";
        }
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}

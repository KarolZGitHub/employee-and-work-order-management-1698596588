package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.password.PasswordResetProcess;
import com.employee.employeeandworkordermanagement.service.UserService;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class MyProfileController {
    private final UserService userService;


    @GetMapping("/my-profile")
    public String showMyProfile(Model model, Authentication authentication) {
        UserDTO userDTO = userService.getUser(authentication);
        model.addAttribute("user", userDTO);
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
}

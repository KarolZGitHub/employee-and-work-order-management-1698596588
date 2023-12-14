package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.password.PasswordResetProcess;
import com.employee.employeeandworkordermanagement.profile.ChangeFirstOrLastName;
import com.employee.employeeandworkordermanagement.service.UserService;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
        UserDTO userDTO = userService.getUserDTO(authentication);
        model.addAttribute("user", userDTO);
        return "myProfile/myProfile";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model, PasswordResetProcess passwordResetProcess, Authentication authentication) {
        UserDTO userDTO = userService.getUserDTO(authentication);
        model.addAttribute("user", userDTO);
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
        model.addAttribute("user", user);
        if (changePasswordSuccess) {
            return "redirect:/profile/my-profile";
        } else {
            model.addAttribute("error", "Passwords are not the same.");
            return "myProfile/error";
        }
    }

    @GetMapping("/change-first-name")
    public String showFirstNameChangeForm(Model model, Authentication authentication, ChangeFirstOrLastName changeFirstOrLastName) {
        UserDTO userDTO = userService.getUserDTO(authentication);
        model.addAttribute("user", userDTO);
        model.addAttribute("changeFirstOrLastName", changeFirstOrLastName);
        return "myProfile/changeFirstName";
    }

    @PostMapping("/change-first-name")
    public String handleFirstNameChange(Model model, ChangeFirstOrLastName changeFirstOrLastName, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        user.setFirstName(changeFirstOrLastName.getFirstOrLastName());
        userService.changeFirstName(user, changeFirstOrLastName.getFirstOrLastName());
        model.addAttribute("user", user);
        return "redirect:/profile/my-profile";
    }

    @GetMapping("/change-last-name")
    public String showLastNameChangeForm(Model model, Authentication authentication, ChangeFirstOrLastName changeFirstOrLastName) {
        UserDTO userDTO = userService.getUserDTO(authentication);
        model.addAttribute("user", userDTO);
        model.addAttribute("changeFirstOrLastName", changeFirstOrLastName);
        return "myProfile/changeLastName";
    }

    @PostMapping("/change-last-name")
    public String handleLastNameChange(Model model, ChangeFirstOrLastName changeFirstOrLastName, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        user.setLastName(changeFirstOrLastName.getFirstOrLastName());
        userService.changeLastName(user, changeFirstOrLastName.getFirstOrLastName());
        model.addAttribute("user", user);
        return "redirect:/profile/my-profile";
    }
}

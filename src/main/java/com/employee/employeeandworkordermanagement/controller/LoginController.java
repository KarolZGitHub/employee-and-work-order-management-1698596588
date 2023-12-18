package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping("/login-success")
    public String showSuccessLoginPage(Authentication authentication, Model model) {
        UserDTO userDTO = userService.getUserDTO(authentication);
        model.addAttribute("user", userDTO);
        return "login/loggedIn";
    }
}

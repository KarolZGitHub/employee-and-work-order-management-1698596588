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
public class MainPageController {
    private final UserService userService;

    @GetMapping("/")
    public String showMainPage(Model model, Authentication authentication) {
        if (authentication == null) {
            return "index";
        }
        UserDTO userDTO = userService.getUserDTO(authentication);
        model.addAttribute("user", userDTO);
        return "index";
    }
}

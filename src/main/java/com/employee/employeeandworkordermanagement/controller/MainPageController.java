package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.user.User;
import com.employee.employeeandworkordermanagement.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final UserService userService;
    @GetMapping("/")
    public String showMainPage(Model model, Authentication authentication) {
        if(authentication==null){
            return "index";
        }
        String email = authentication.getName();
        User user = userService.findByEmail(email).get();
        model.addAttribute("user", user);
        return "index";
    }
}

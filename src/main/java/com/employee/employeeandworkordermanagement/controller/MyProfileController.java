package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}

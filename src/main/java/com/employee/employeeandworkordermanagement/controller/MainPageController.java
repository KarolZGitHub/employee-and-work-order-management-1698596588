package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final UserService userService;

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }

    @ModelAttribute("blockDesignerView")
    public boolean blockDesignerAccess(Authentication authentication) {
        if (authentication != null) {
            User user = userService.findOptionalUserByEmail(authentication.getName()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found"));
            if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.OPERATOR)) {
                return true;
            } else return false;
        } else {
            return false;
        }
    }

    @GetMapping("/")
    public String showMainPage() {
        return "index";
    }
}

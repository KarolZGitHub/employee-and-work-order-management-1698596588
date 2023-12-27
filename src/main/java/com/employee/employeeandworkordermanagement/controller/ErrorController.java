package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class ErrorController {
    private final UserService userService;
    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorCode", HttpStatus.FORBIDDEN.value());
                model.addAttribute("errorMessage", "Access Denied");
                return "error/403";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());
                model.addAttribute("errorMessage", "Page Not Found");
                return "error/404";
            }
        }
        model.addAttribute("errorCode", "Unknown");
        model.addAttribute("errorMessage", "An error occurred");
        return "error/error";
    }
}

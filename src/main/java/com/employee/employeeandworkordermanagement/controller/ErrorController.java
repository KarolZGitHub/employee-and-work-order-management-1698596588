package com.employee.employeeandworkordermanagement.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ErrorController {
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

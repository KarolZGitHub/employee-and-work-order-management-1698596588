package com.employee.employeeandworkordermanagement.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogOutController {
    @GetMapping("/logout")
    public String handleLogout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "index";
    }
    @GetMapping("/logout-success")
    public String handleSuccessLogout(){
        return "logout/successfulLogOut";
    }
}

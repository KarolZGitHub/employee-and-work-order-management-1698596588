package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.user.User;
import com.employee.employeeandworkordermanagement.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/panel")
    public String showAdminPanel(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "admin/adminPanel";
    }

    @GetMapping("/allUsers")
    public String showAllUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            Model model, Authentication authentication) {
        Page<User> userPage = userService.getAllUsers(PageRequest.of(page, size));
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("userPage", userPage);
        return "admin/allUsers";
    }

    @PostMapping("/change-role/{id}")
    public String changeRoleHandler(@PathVariable(name = "id") long id, @RequestParam Role role) {
        User theUser = userService.findById(id);
        theUser.setRole(role);
        userService.editUserRole(theUser, role);
        return "redirect:/admin/allUsers";
    }
}

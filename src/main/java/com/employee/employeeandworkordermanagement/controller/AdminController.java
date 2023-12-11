package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.dto.UserDTO;
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
        UserDTO userDTO = userService.getUser(authentication);
        model.addAttribute("user", userDTO);
        return "admin/adminPanel";
    }

    @GetMapping("/allUsers")
    public String showAllUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            Model model, Authentication authentication) {
        Page<User> userPage = userService.getAllUsers(PageRequest.of(page, size));
        UserDTO userDTO = userService.getUser(authentication);
        model.addAttribute("user", userDTO);
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

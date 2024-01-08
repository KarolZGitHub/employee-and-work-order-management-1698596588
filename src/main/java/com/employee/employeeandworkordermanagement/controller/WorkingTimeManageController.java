package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.service.UserService;
import com.employee.employeeandworkordermanagement.service.WorkingTimeService;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/work-manage")
public class WorkingTimeManageController {
    private final WorkingTimeService workingTimeService;
    private final UserService userService;

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }
    @GetMapping("/create-working-time/{id}")
    public String handleAddWorkingTimeForm(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        workingTimeService.createWorkDay(user);
        return "redirect:/designer/all-designers";
    }
    @GetMapping("users-working-time")
    public String showUsersWorkingTime(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "asc") String direction,
                                       @RequestParam(required = false, defaultValue = "id") String sortField,
                                       Model model){
        //TODO:implement
        return "/work/workingTimeForUser";
    }
    //TODO:features for admin and operator
    // to stop working time, delete working time
}
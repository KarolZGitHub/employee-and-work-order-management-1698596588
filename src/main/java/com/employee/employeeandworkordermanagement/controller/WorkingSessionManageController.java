package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.service.TaskService;
import com.employee.employeeandworkordermanagement.service.UserService;
import com.employee.employeeandworkordermanagement.service.WorkingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/work-manage")
public class WorkingSessionManageController {
    private final WorkingSessionService workingSessionService;
    private final UserService userService;
    private final TaskService taskService;

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }
    @GetMapping("users-working-time")
    public String showUsersWorkingTime(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "asc") String direction,
                                       @RequestParam(required = false, defaultValue = "id") String sortField,
                                       Model model) {
        //TODO:implement
        return "/work/workingTimeForUser";
    }
    //TODO:features for admin and operator
    // to stop working time, delete working time
}

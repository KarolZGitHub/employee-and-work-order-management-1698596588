package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.service.ArchivedTaskService;
import com.employee.employeeandworkordermanagement.service.TaskService;
import com.employee.employeeandworkordermanagement.service.UserService;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/edit")
public class EditTaskController {
    private final TaskService taskService;
    private final UserService userService;

    @ModelAttribute("designers")
    public List<User> getDesigners() {
        return userService.getDesigners();
    }

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }

    @GetMapping("/edit-task")
    public String editTaskDetails(@RequestParam Long id, Model model) {
        Task task = taskService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Task has not been found."));
        model.addAttribute("task", task);
        return "task/editTask";
    }

    @PostMapping("/edit-task")
    public String handleEditTask(@RequestParam Long id, Task task, BindingResult bindingResult, Model model,
                                 Authentication authentication) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        taskService.editTask(task, authentication);
        return "redirect:/task/all-tasks";
    }
}

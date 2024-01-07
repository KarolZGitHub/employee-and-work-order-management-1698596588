package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.service.TaskService;
import com.employee.employeeandworkordermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskRestController {
    private final TaskService taskService;
    private final UserService userService;

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public ModelAndView createTask(@Valid Task task, BindingResult result, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.setViewName("error/error");
            modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
            modelAndView.addObject("errorList", result.getAllErrors());
        } else {
            try {
                taskService.createTask(task, authentication);

                modelAndView.setViewName("task/success");
                modelAndView.addObject("status", HttpStatus.CREATED.value());
                modelAndView.addObject("message", "Task created successfully");
                modelAndView.addObject("task", task);
            } catch (Exception e) {

                modelAndView.setViewName("error/error");
                modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                modelAndView.addObject("errorList", Collections.singletonList(e.getMessage()));
            }
        }

        return modelAndView;
    }
}

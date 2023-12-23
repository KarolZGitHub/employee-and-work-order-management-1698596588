package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.Designer;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.service.DesignerService;
import com.employee.employeeandworkordermanagement.service.TaskService;
import com.employee.employeeandworkordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/task")
public class TaskController {
    private final DesignerService designerService;
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

    @ModelAttribute("designers")
    public List<Designer> getDesigners() {
        return designerService.findAll();
    }

    @GetMapping("/add")
    public String showAddTaskForm(Task task, Model model) {
        model.addAttribute("designers", designerService.findAll());
        model.addAttribute("task", task);
        return "task/addTaskForm";
    }

    @GetMapping("/edit-task")
    public String editTaskDetails(@RequestParam Long id, Model model) {
        Task task = taskService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Task has not been found."));
        model.addAttribute("task", task);
        return "task/editTask";
    }

    @PostMapping("/edit-task")
    public String handleEditTask(@RequestParam Long id, Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        taskService.editTask(task);
        return "redirect:/task/all-tasks";
    }

    @GetMapping("/all-tasks")
    public String showAllTasks(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "50") int size, Model model) {
        Page<Task> taskPage = taskService.getAllTasks(PageRequest.of(page, size));
        model.addAttribute("taskPage", taskPage);
        return "task/tasks";
    }

    @GetMapping("/delete-task")
    public String deleteTask(@RequestParam(name = "id") Long id) {
        taskService.deleteTask(taskService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Task has not been found.")));
        return "redirect:/task/all-tasks";
    }

    @GetMapping("/close-task")
    public String closeTask(@RequestParam(name = "id") Long id) {
        taskService.closeTask(taskService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Task has not been found.")));
        return "redirect:/task/all-tasks";
    }
}

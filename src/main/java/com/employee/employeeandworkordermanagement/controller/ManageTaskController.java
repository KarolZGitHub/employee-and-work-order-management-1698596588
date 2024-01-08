package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.service.TaskService;
import com.employee.employeeandworkordermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/edit")
public class ManageTaskController {
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

    @GetMapping("/add")
    public String showAddTaskForm(Task task, Model model) {
        model.addAttribute("task", task);
        return "task/addTaskForm";
    }

    @PostMapping("/add")
    public String handleAddTaskForm(@Valid Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        taskService.createTask(task);
        model.addAttribute("message","Task has been successfully created.");
        return "task/taskHasBeenCreated";
    }

    @GetMapping("/edit-task")
    public String editTaskDetails(@RequestParam Long id, Model model) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        return "task/editTask";
    }

//    @PostMapping("/edit-task")
//    public String handleEditTask(@RequestParam Long id, Task task, BindingResult bindingResult, Model model,
//                                 Authentication authentication) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("errorList", bindingResult.getAllErrors());
//            return "error/error";
//        }
//        taskService.editTask(task, authentication);
//        return "redirect:/task/all-tasks";
//    }

//    @GetMapping("/delete-task")
//    public String deleteTask(@RequestParam(name = "id") Long id, Authentication authentication) {
//        taskService.deleteTask(taskService.findById(id), authentication);
//        return "redirect:/task/all-tasks";
//    }

//    @GetMapping("/close-task")
//    public String closeTask(@RequestParam(name = "id") Long id, Authentication authentication) {
//        taskService.closeTask(taskService.findById(id), authentication);
//        return "redirect:/task/all-tasks";
//    }

    //    @GetMapping("/archive-task")
//    public String archiveTask(@RequestParam(name = "id") Long id) {
//        Task task = taskService.findById(id);
//        taskService.archiveTask(task);
//        return "redirect:/task/archived-tasks";
//    }
//    @GetMapping("/activate-task")
//    private String activateTask(@RequestParam(name = "id") Long id) {
//        Task task = taskService.findById(id);
//        taskService.setTaskToActive(task);
//        return "redirect:/task/all-tasks";
//    }
}

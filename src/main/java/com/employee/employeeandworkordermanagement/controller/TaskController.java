package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.ArchivedTask;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.feedback.FeedbackRequest;
import com.employee.employeeandworkordermanagement.service.ArchivedTaskService;
import com.employee.employeeandworkordermanagement.service.TaskService;
import com.employee.employeeandworkordermanagement.service.UserService;
import com.employee.employeeandworkordermanagement.user.User;
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
    private final TaskService taskService;
    private final UserService userService;
    private final ArchivedTaskService archivedTaskService;

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }

    @ModelAttribute("designers")
    public List<User> getDesigners() {
        return userService.getDesigners();
    }

    @GetMapping("/add")
    public String showAddTaskForm(Task task, Model model) {
        model.addAttribute("task", task);
        return "task/addTaskForm";
    }

    @GetMapping("/all-tasks")
    public String showAllTasks(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "50") int size, Model model) {
        Page<Task> taskPage = taskService.getAllTasks(PageRequest.of(page, size));
        model.addAttribute("taskPage", taskPage);
        return "task/tasks";
    }

    @GetMapping("/delete-task")
    public String deleteTask(@RequestParam(name = "id") Long id, Authentication authentication) {
        taskService.deleteTask(taskService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task has not been found.")), authentication);
        return "redirect:/task/all-tasks";
    }

    @GetMapping("/close-task")
    public String closeTask(@RequestParam(name = "id") Long id, Authentication authentication) {
        taskService.closeTask(taskService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task has not been found.")), authentication);
        return "redirect:/task/all-tasks";
    }

    @GetMapping("/archived-tasks")
    public String showAllArchivedTasks(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "50") int size,
                                       Model model) {
        Page<ArchivedTask> archivedTaskPage = archivedTaskService.getAllArchivedTasks(PageRequest.of(page, size));
        model.addAttribute("archivedTaskPage", archivedTaskPage);
        return "task/archivedTasks";
    }

    @GetMapping("/archive-task")
    public String archiveTask(@RequestParam(name = "id") Long id, Authentication authentication) {
        Task task = taskService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Task has not been found"));
        archivedTaskService.archiveTask(task);
        taskService.deleteTask(task, authentication);
        return "redirect:/task/archived-tasks";
    }

    @PostMapping("/task-feedback")
    public String handleFeedback(@RequestParam(name = "id") Long id, @ModelAttribute FeedbackRequest feedbackRequest,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        ArchivedTask archivedTask = archivedTaskService.findById(id);
        archivedTask.setFeedback(feedbackRequest.getFeedback());
        archivedTask.setDifficulty(feedbackRequest.getDifficulty());
        archivedTaskService.saveArchivedTask(archivedTask);
        return "redirect:/task/archived-tasks";
    }

    @GetMapping("/task-feedback")
    public String showFeedbackForm(@RequestParam(name = "id") Long id, FeedbackRequest feedbackRequest,Model model) {
        model.addAttribute("id",id);
        model.addAttribute("feedbackRequest",feedbackRequest);
        return ("task/feedbackForm");
    }
}
//TODO "edit" buttons, fix email sending, add boolean to archived tasks to prevent multiple feedback

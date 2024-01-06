package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.data.Role;
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
import org.springframework.data.domain.Sort;
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

    @ModelAttribute("blockDesignerView")
    public boolean blockDesignerAccess(Authentication authentication) {
        if (authentication != null) {
            User user = userService.findByEmail(authentication.getName()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found"));
            if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.OPERATOR)) {
                return true;
            } else return false;
        } else {
            return false;
        }
    }

    @ModelAttribute("designers")
    public List<User> getDesigners() {
        return userService.getDesigners();
    }

    @GetMapping("/all-tasks")
    public String showAllTasks(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "asc") String direction,
                               @RequestParam(required = false, defaultValue = "id") String sortField,
                               Model model) {
        model.addAttribute("sortField", sortField);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Page<Task> taskPage = taskService.getAllTasks(PageRequest.of(page, 50, sort));
        model.addAttribute("taskPage", taskPage);
        return "task/tasks";
    }

    @GetMapping("/archived-tasks")
    public String showAllArchivedTasks(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "asc") String direction,
                                       @RequestParam(required = false, defaultValue = "id") String sortField,
                                       Model model) {
        model.addAttribute("sortField", sortField);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Page<ArchivedTask> archivedTaskPage = archivedTaskService.getAllArchivedTasks(PageRequest.of(
                page, 50, sort));
        model.addAttribute("archivedTaskPage", archivedTaskPage);
        return "task/archivedTasks";
    }

    @PostMapping("/task-feedback")
    public String handleFeedback(@RequestParam(name = "id") Long id,
                                 @ModelAttribute FeedbackRequest feedbackRequest,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        ArchivedTask archivedTask = archivedTaskService.findById(id);
        archivedTask.setFeedback(feedbackRequest.getFeedback());
        archivedTask.setDifficulty(feedbackRequest.getDifficulty());
        archivedTask.setFeedbackSet(true);
        archivedTaskService.saveArchivedTask(archivedTask);
        return "redirect:/task/archived-tasks";
    }

    @GetMapping("/task-feedback")
    public String showFeedbackForm(@RequestParam(name = "id") Long id, FeedbackRequest feedbackRequest,
                                   Model model,
                                   Authentication authentication) {
        ArchivedTask archivedTask = archivedTaskService.findById(id);
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User has not been found"));
        if (!archivedTask.getDesigner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not right designer");
        }
        model.addAttribute("id", id);
        model.addAttribute("feedbackRequest", feedbackRequest);
        return ("task/feedbackForm");
    }
}
//TODO sorting in admin panel, task and archived task ascending and descending

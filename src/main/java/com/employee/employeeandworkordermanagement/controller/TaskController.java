package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.service.TaskFeedbackService;
import com.employee.employeeandworkordermanagement.service.TaskService;
import com.employee.employeeandworkordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskFeedbackService taskFeedbackService;

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
            User user = userService.findOptionalUserByEmail(authentication.getName()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found"));
            if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.OPERATOR)) {
                return true;
            } else return false;
        } else {
            return false;
        }
    }
    @GetMapping("/all-tasks")
    public String showAllTasks(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "asc") String direction,
                               @RequestParam(required = false, defaultValue = "id") String sortField,
                               Model model) {
        model.addAttribute("sortField", sortField);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Page<Task> taskPage = taskService.getUnarchivedTasksPage(PageRequest.of(page, 50, sort));
        model.addAttribute("taskPage", taskPage);
        return "task/tasks";
    }
}

//    @GetMapping("/archived-tasks")
//    public String showAllArchivedTasks(@RequestParam(required = false, defaultValue = "0") int page,
//                                       @RequestParam(required = false, defaultValue = "asc") String direction,
//                                       @RequestParam(required = false, defaultValue = "id") String sortField,
//                                       Model model) {
//        model.addAttribute("sortField", sortField);
//        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
//        Page<Task> archivedTaskPage = taskService.getAllArchivedTasks(PageRequest.of(
//                page, 50, sort));
//        model.addAttribute("archivedTaskPage", archivedTaskPage);
//        return "task/archivedTasks";


//    @PostMapping("/task-feedback")
//    public String handleFeedback(@RequestParam(name = "id") Long id,
//                                 @ModelAttribute FeedbackRequest feedbackRequest,
//                                 BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("errorList", bindingResult.getAllErrors());
//            return "error/error";
//        }
//        Task task = taskService.findById(id);
//        taskFeedbackService.addFeedback(task, feedbackRequest);
//        return "redirect:/task/archived-tasks";
//    }

//    @GetMapping("/task-feedback")
//    public String showFeedbackForm(@RequestParam(name = "id") Long id, FeedbackRequest feedbackRequest,
//                                   Model model,
//                                   Authentication authentication) {
//        Task task = taskService.findById(id);
//        User user = userService.findByEmail(authentication.getName()).orElseThrow(
//                () -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User has not been found"));
//        if (!task.getDesigner().equals(user)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not right designer");
//        }
//        model.addAttribute("id", id);
//        model.addAttribute("feedbackRequest", feedbackRequest);
//        return ("task/feedbackForm");
//    }
//TODO sorting in admin panel, task and archived task ascending and descending

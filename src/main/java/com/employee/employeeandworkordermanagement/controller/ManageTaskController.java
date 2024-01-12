package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.feedback.FeedbackRequest;
import com.employee.employeeandworkordermanagement.service.TaskFeedbackService;
import com.employee.employeeandworkordermanagement.service.TaskService;
import com.employee.employeeandworkordermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.ModuleElement;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/edit")
public class ManageTaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskFeedbackService taskFeedbackService;

    @ModelAttribute("availableDesigners")
    public List<User> getAvailableDesigners() {
        return userService.getAvailableDesigners();
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
    public String handleAddTaskForm(@Valid Task task, BindingResult bindingResult, Model model,
                                    Authentication authentication) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        taskService.createTask(task, authentication);
        model.addAttribute("message", "Task has been successfully created.");
        return "task/taskHasBeenCreated";
    }

    @GetMapping("/edit-task")
    public String showEditTaskForm(@RequestParam Long id, Model model) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        return "task/editTask";
    }

    @PostMapping("/edit-task")
    public String handleEditTask(@RequestParam Long id, @Valid Task task, BindingResult bindingResult,
                                 Model model, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        taskService.editTask(id, task, authentication);
        return "redirect:/task/all-tasks";
    }

    @GetMapping("/archived-tasks")
    public String showAllArchivedTasks(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "asc") String direction,
                                       @RequestParam(required = false, defaultValue = "id") String sortField,
                                       Model model) {
        model.addAttribute("sortField", sortField);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Page<Task> archivedTaskPage = taskService.getAllArchivedTasksPage(PageRequest.of(
                page, 50, sort));
        model.addAttribute("archivedTaskPage", archivedTaskPage);
        return "task/archivedTasks";
    }

    @GetMapping("/archive-task")
    public String archiveTask(@RequestParam(name = "taskId") Long taskId, Authentication authentication) {
        taskService.archiveTask(taskId, authentication);
        return "redirect:/edit/archived-tasks";
    }

    @GetMapping("/activate-task")
    private String activateTask(@RequestParam(name = "taskId") Long taskId, Authentication authentication) {
        taskService.setTaskToActive(taskId, authentication);
        return "redirect:/task/all-tasks";
    }
    @GetMapping("/set-feedback")
    public String showFeedbackForm(@RequestParam(name = "id")Long taskId, FeedbackRequest feedbackRequest, Model model){
        model.addAttribute("id", taskId);
        model.addAttribute("feedbackRequest", feedbackRequest);
        return "task/feedbackForm";
    }
    @PostMapping("/set-feedback")
    public String handleFeedbackForm(@RequestParam(name = "id") Long taskId,@Valid FeedbackRequest feedbackRequest,
                                     BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("errorList", bindingResult.getAllErrors());
            return "error/error";
        }
        taskFeedbackService.addFeedback(taskId,feedbackRequest);
        return "redirect:/task/all-tasks";
    }
}

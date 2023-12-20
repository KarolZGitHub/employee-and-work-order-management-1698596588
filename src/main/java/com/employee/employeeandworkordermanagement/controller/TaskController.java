package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.service.DesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final DesignerService designerService;

    @GetMapping("/add")
    public String showAddTaskForm(Task task, Model model) {
        model.addAttribute("designers", designerService.findAll());
        model.addAttribute("task", task);
        return "task/addTaskForm";
    }
}

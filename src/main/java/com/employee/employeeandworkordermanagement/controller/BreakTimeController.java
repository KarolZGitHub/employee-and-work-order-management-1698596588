package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.service.BreakTimeService;
import com.employee.employeeandworkordermanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/break-time")
public class BreakTimeController {
    private final BreakTimeService breakTimeService;
    private final TaskService taskService;

    public String handleStartBreakTime(@RequestParam(name = "id") Long id, Authentication authentication) {
        Task task = taskService.findById(id);
        breakTimeService.startBreakTime(task, authentication);
        return "redirect:/task/your-task";
    }
//TODO: handle duration of working time with break time
}

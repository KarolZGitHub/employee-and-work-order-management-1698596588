package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskRestController {
    private final TaskService taskService;
    @GetMapping
    public List<Task> getAllTasks(){
        return taskService.getAllTasks();
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<Task>getTaskById(@PathVariable Long taskId){
        Optional <Task> task = taskService.findById(taskId);
        if(task.isPresent()){
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ModelAndView createTask(@Valid Task task, BindingResult result){
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.setViewName("error/error");
            modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
            modelAndView.addObject("errorList",result.getAllErrors());
        } else {
            try {
                taskService.createTask(task);

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

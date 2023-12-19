package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }
    public Optional<Task> findById(Long id){
       return taskRepository.findById(id);
    }
    public void createTask(Task task){
        taskRepository.save(task);
    }
}

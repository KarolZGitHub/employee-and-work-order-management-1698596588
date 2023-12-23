package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public void createTask(Task task) {
        taskRepository.save(task);
    }

    public void editTask(Task task) {
        task.setEditedAt(new Date());
        taskRepository.save(task);
    }
    public void closeTask(Task task){
        task.setTaskStatus(TaskStatus.CLOSED);
        taskRepository.save(task);
    }
    public void deleteTask(Task task){
        taskRepository.delete(task);
    }
    public Page<Task> getAllTasks(PageRequest pageRequest) {
        return taskRepository.findAll(pageRequest);
    }
}

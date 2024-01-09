package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final MessageService messageService;
    private final UserService userService;

    public void createTask(Task task, Authentication authentication) {
        User sender = userService.findUserByEmail(authentication.getName());
        messageService.notifyDesignerIfAssignedToTask(task.getDesigner(), sender, task);
        task.setTaskStatus(TaskStatus.PENDING);
        taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Task has not been found."));
    }

    public Page<Task> getAllTasksPage(PageRequest pageRequest) {
        return taskRepository.findAll(pageRequest);
    }

    public void editTask(Long id, Task updatedTask, Authentication authentication) {
        User sender = userService.findUserByEmail(authentication.getName());
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task has not been found."));
        if (!updatedTask.getDesigner().equals(task.getDesigner())) {
            messageService.notifyChangeDesignerInTask(task.getDesigner(), updatedTask.getDesigner(), sender, task);
        } else {
            messageService.notifyDesignerIfTaskIsEdited(updatedTask.getDesigner(), sender, task);
        }
        task.setTaskName(updatedTask.getTaskName());
        task.setDescription(updatedTask.getDescription());
        task.setDesigner(updatedTask.getDesigner());
        taskRepository.save(task);
    }
}

package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.repository.TaskRepository;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final MessageService messageService;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public void createTask(Task task, Authentication authentication) {
        messageService.notifyDesignerIfAssignedToTask(task.getDesigner(), userService.findByEmail(authentication.getName()).get(),
                task);
        taskRepository.save(task);
    }

    public void editTask(Task task, Authentication authentication) {
        messageService.notifyDesignerIfTaskIsEdited(task.getDesigner(), userService.findByEmail(authentication.getName()).get(),
                task);
        task.setEditedAt(new Date());
        taskRepository.save(task);
    }

    public void closeTask(Task task, Authentication authentication) {
        messageService.notifyDesignerIfClosedTask(task.getDesigner(), userService.findByEmail(authentication.getName()).get(),
                task);
        task.setTaskStatus(TaskStatus.CLOSED);
        taskRepository.save(task);
    }

    public void deleteTask(Task task, Authentication authentication) {
        User user = task.getDesigner();
        messageService.notifyDesignerIfTaskIsDeleted(user, userService.findByEmail(authentication.getName()).get(), task);
        taskRepository.delete(task);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public Page<Task> getAllTasks(PageRequest pageRequest) {
        return taskRepository.findAll(pageRequest);
    }
}

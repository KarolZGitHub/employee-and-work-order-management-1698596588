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

import java.util.List;

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

    public Task findTaskByUser(User designer) {
        return taskRepository.findByDesigner(designer).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task has not been found."));
    }

    public Page<Task> getAllUnarchivedTasksPage(PageRequest pageRequest) {
        return taskRepository.findAllByTaskStatusNot(TaskStatus.ARCHIVED, pageRequest);
    }

    public Page<Task> getAllArchivedTasksPage(PageRequest pageRequest) {
        return taskRepository.findAllByTaskStatus(TaskStatus.ARCHIVED, pageRequest);
    }

    public void archiveTask(Long taskId, Authentication authentication) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task has not been found."));
        if (!task.getTaskStatus().equals(TaskStatus.DONE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You cannot archive undone task.");
        }
        User sender = userService.findUserByEmail(authentication.getName());
        List<User> operatorsList = userService.getAllOperators();
        task.setTaskStatus(TaskStatus.ARCHIVED);
        operatorsList.forEach(operator -> messageService.notifyOperatorThatTaskIsArchived(operator, sender, task));
        taskRepository.save(task);
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

    public void markTaskAsComplete(Long id, Authentication authentication) {
        User sender = userService.findUserByEmail(authentication.getName());
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task has not been found."));
        List<User> operatorsList = userService.getAllOperators();
        operatorsList.forEach(operator -> messageService.notifyOperatorThatTaskIsCompleted(operator, sender, task));
        task.setTaskStatus(TaskStatus.DONE);
        taskRepository.save(task);
    }

    public void setTaskToActive(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Task has not been found."));
        task.setTaskStatus(TaskStatus.ACTIVE);
        taskRepository.save(task);
    }
}

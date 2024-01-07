package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.repository.TaskRepository;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final MessageService messageService;
    private final TaskFeedbackService taskFeedbackService;
    private final WorkingTimeService workingTimeService;
    private final WorkingTimeHistoryService workingTimeHistoryService;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Task has not been found."));
    }

    public void createTask(Task task, Authentication authentication) {
        messageService.notifyDesignerIfAssignedToTask(task.getDesigner(), userService.findByEmail(authentication.getName()).get(),
                task);
        taskRepository.save(task);
        taskFeedbackService.initializeFeedback(task);
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
        workingTimeHistoryService.convertAndSaveWorkingTimeToHistory(task.getWorkingTime());
        taskRepository.delete(task);
    }
    public Page<Task> getUnarchivedPage(PageRequest pageRequest) {
        return taskRepository.findAllByTaskStatusNot(TaskStatus.ARCHIVED, pageRequest);
    }

    public Page<Task> getAllArchivedTasks(PageRequest pageRequest) {
        return taskRepository.findAllByTaskStatus(TaskStatus.ARCHIVED, pageRequest);
    }

    public void archiveTask(Task task) {
        if (task.getTaskStatus().equals(TaskStatus.ARCHIVED) || task.getTaskStatus().equals(TaskStatus.ACTIVE) ||
                task.getTaskStatus().equals(TaskStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You cannot archive this task");
        }
        task.setTaskStatus(TaskStatus.ARCHIVED);
        taskRepository.save(task);
    }

    public void setTaskToActive(Task task) {
        if (task.getTaskStatus().equals(TaskStatus.ACTIVE) || task.getTaskStatus().equals(TaskStatus.ARCHIVED) ||
                task.getTaskStatus().equals(TaskStatus.CLOSED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You cannot activate this task");
        }
        workingTimeService.initializeWorkingTime(task);
        task.setTaskStatus(TaskStatus.ACTIVE);
        taskRepository.save(task);
    }
}

package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.ArchivedTask;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.repository.ArchivedTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchivedTaskService {
    private final ArchivedTaskRepository archivedTaskRepository;
    private final TaskService taskService;
    private final MessageService messageService;
    private final UserService userService;

    public Page<ArchivedTask> getAllArchivedTasksPage(PageRequest pageRequest) {
        return archivedTaskRepository.findAll(pageRequest);
    }
    public void archiveTask(Long taskId, Authentication authentication){
        User sender = userService.findUserByEmail(authentication.getName());
        Task task = taskService.findById(taskId);
        List<User> operatorsList = userService.getAllOperators();
        ArchivedTask archivedTask = new ArchivedTask();
        archivedTask.setTaskName(task.getTaskName());
        archivedTask.setTaskFeedback(task.getTaskFeedback().getFeedback());
        archivedTask.setDesigner(task.getDesigner());
        archivedTask.setDescription(task.getDescription());
        archivedTask.setGrade(task.getTaskFeedback().getGrade());
        messageService.notifyDesignerThatTaskIsArchived(task.getDesigner(),sender,task);
        operatorsList.forEach(operator -> messageService.notifyOperatorThatTaskIsArchived(operator, sender, task));
        //TODO:add total working time
        archivedTaskRepository.save(archivedTask);
        taskService.deleteTask(task);
    }
}

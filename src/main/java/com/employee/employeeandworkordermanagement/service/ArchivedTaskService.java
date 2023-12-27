package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.ArchivedTask;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.repository.ArchivedTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ArchivedTaskService {
    private final ArchivedTaskRepository archivedTaskRepository;

    public void archiveTask(Task task) {
        ArchivedTask archivedTask = new ArchivedTask(task.getTaskName(), task.getDescription(),
                task.getDesigner(), task.getCreatedAt());
        archivedTaskRepository.save(archivedTask);
    }

    public Page<ArchivedTask> getAllArchivedTasks(PageRequest pageRequest) {
        return archivedTaskRepository.findAll(pageRequest);
    }
    public ArchivedTask findById(Long id){
        return archivedTaskRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Archived task has not been found."));
    }
    public void saveArchivedTask(ArchivedTask archivedTask){
        archivedTaskRepository.save(archivedTask);
    }
}

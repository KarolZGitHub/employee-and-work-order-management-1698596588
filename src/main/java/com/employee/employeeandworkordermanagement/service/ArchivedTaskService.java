package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.ArchivedTask;
import com.employee.employeeandworkordermanagement.repository.ArchivedTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArchivedTaskService {
    private final ArchivedTaskRepository archivedTaskRepository;

    public Page<ArchivedTask> getAllArchivedTasksPage(PageRequest pageRequest) {
        return archivedTaskRepository.findAll(pageRequest);
    }
}

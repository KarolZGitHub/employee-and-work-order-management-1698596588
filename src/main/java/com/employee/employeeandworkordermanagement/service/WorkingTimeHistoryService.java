package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.WorkingTime;
import com.employee.employeeandworkordermanagement.entity.WorkingTimeHistory;
import com.employee.employeeandworkordermanagement.repository.WorkingTimeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkingTimeHistoryService {
    private final WorkingTimeHistoryRepository workingTimeHistoryRepository;

    @Transactional
    public void convertAndSaveWorkingTimeToHistory(WorkingTime workingTime) {
        WorkingTimeHistory workingTimeHistory = new WorkingTimeHistory();
        // Assignment data to new WorkTime object.
        workingTimeHistory.setCreatedAt(workingTime.getCreatedAt());
        workingTimeHistory.setWorkStarted(workingTime.getWorkStarted());
        workingTimeHistory.setWorkFinished(workingTime.getWorkFinished());
        workingTimeHistory.setUser(workingTime.getUser());
        workingTimeHistory.setTask(workingTime.getTask());
        // Save to database.
        workingTimeHistoryRepository.save(workingTimeHistory);
    }
}

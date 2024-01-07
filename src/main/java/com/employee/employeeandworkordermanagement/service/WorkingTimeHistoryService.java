package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.WorkingTime;
import com.employee.employeeandworkordermanagement.entity.WorkingTimeHistory;
import com.employee.employeeandworkordermanagement.repository.WorkingTimeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkingTimeHistoryService {
    private final WorkingTimeHistoryRepository workingTimeHistoryRepository;

    public void convertAndSaveWorkingTimeToHistory(WorkingTime workingTime) {
        WorkingTimeHistory workingTimeHistory = new WorkingTimeHistory();
        workingTimeHistory.setCreatedAt(workingTime.getCreatedAt());
        workingTimeHistory.setWorkStarted(workingTime.getWorkStarted());
        workingTimeHistory.setWorkFinished(workingTime.getWorkFinished());
        workingTimeHistory.setOverallWorkingTime(workingTime.getOverallWorkingTime());
        workingTimeHistory.setUser(workingTime.getUser());
        workingTimeHistoryRepository.save(workingTimeHistory);
    }
}

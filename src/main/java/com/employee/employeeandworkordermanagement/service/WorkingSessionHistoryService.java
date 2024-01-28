package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.WorkingSession;
import com.employee.employeeandworkordermanagement.entity.WorkingSessionHistory;
import com.employee.employeeandworkordermanagement.repository.WorkingSessionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkingSessionHistoryService {
    private final WorkingSessionHistoryRepository workingSessionHistoryRepository;

    @Transactional
    public void convertAndSaveWorkingTimeToHistory(WorkingSession workingSession) {
        WorkingSessionHistory workingSessionHistory = new WorkingSessionHistory();
        // Assignment data to new WorkTime object.
        workingSessionHistory.setCreatedAt(workingSession.getCreatedAt());
        workingSessionHistory.setWorkStarted(workingSession.getWorkStarted());
        workingSessionHistory.setWorkFinished(workingSession.getWorkFinished());
        workingSessionHistory.setUser(workingSession.getUser());
        workingSessionHistory.setTask(workingSession.getTask());
        // Save to database.
        workingSessionHistoryRepository.save(workingSessionHistory);
    }
}

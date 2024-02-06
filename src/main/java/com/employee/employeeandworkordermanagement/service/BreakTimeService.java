package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.BreakTime;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.WorkingSession;
import com.employee.employeeandworkordermanagement.repository.BreakTimeRepository;
import com.employee.employeeandworkordermanagement.repository.WorkingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreakTimeService {
    private final BreakTimeRepository breakTimeRepository;
    private final WorkingSessionRepository workingSessionRepository;

    public Duration calculateBreakDuration(BreakTime breakTime) {
        return Duration.between(breakTime.getStartTime(), breakTime.getFinishTime());
    }

    public void startBreakTime(Task task) {
        List<WorkingSession> workingSessions = workingSessionRepository.findAllByUser(task.getDesigner());
        WorkingSession workingSession = workingSessions.stream().filter(WorkingSession::isActive).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Working Session has not been found."));
        BreakTime breakTime = new BreakTime();
        breakTime.setStartTime(Instant.now());
        breakTime.setWorkingSession(workingSession);
        breakTimeRepository.save(breakTime);
    }

    public void stopBreakTime(Task task) {
        List<WorkingSession> workingSessions = workingSessionRepository.findAllByUser(task.getDesigner());
        WorkingSession workingSession = workingSessions.stream().filter(WorkingSession::isActive).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Working Session has not been found."));
        List<BreakTime> breakTimeList = workingSession.getBreakTimes();
        BreakTime breakTime = breakTimeList.stream().filter(BreakTime::isActive).findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Breaking time has not been found."));
        breakTime.setFinishTime(Instant.now());
        breakTime.setActive(false);
        breakTimeRepository.save(breakTime);
    }
}

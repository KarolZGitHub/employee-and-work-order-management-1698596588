package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.BreakTime;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.WorkingSession;
import com.employee.employeeandworkordermanagement.repository.BreakTimeRepository;
import com.employee.employeeandworkordermanagement.repository.WorkingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;

    public Duration workingSessionDurationWithBreaks(List<BreakTime> breakTimeList, WorkingSession workingSession) {
        Duration totalBreaksDuration = breakTimeList.stream()
                .filter(b -> b.getStartTime().isBefore(b.getFinishTime()))
                .map(b -> Duration.between(b.getStartTime(), b.getFinishTime()))
                .reduce(Duration.ZERO, Duration::plus);
        Duration workingSessionDuration = Duration.between(workingSession.getWorkStarted(),
                workingSession.getWorkFinished());
        if (totalBreaksDuration.compareTo(workingSessionDuration) > 0) {
            return Duration.ZERO;
        } else {
            return workingSessionDuration.minus(totalBreaksDuration);
        }
    }

    public void startBreakTime(Task task, Authentication authentication) {
        if(!task.getDesigner().equals(userService.findUserByEmail(authentication.getName()))){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Wrong user");
        }
        List<WorkingSession> workingSessions = workingSessionRepository.findAllByUser(task.getDesigner());
        WorkingSession workingSession = workingSessions.stream().filter(WorkingSession::isActive).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Working Session has not been found."));
        BreakTime breakTime = new BreakTime();
        breakTime.setStartTime(Instant.now());
        breakTime.setWorkingSession(workingSession);
        breakTimeRepository.save(breakTime);
    }

    public void stopBreakTime(Task task, Authentication authentication) {
        if(!task.getDesigner().equals(userService.findUserByEmail(authentication.getName()))){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Wrong user");
        }
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

    public boolean showStopButton(Task task) {
        List<WorkingSession> workingSessions = workingSessionRepository.findAllByUser(task.getDesigner());
        WorkingSession workingSession = workingSessions.stream().filter(WorkingSession::isActive).findFirst()
                .orElse(new WorkingSession());
        List<BreakTime> breakTimeList = workingSession.getBreakTimes();
        return breakTimeList.stream().anyMatch(BreakTime::isActive);
    }
}

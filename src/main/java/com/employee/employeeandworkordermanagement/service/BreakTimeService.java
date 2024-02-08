package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.BreakTime;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.entity.WorkingSession;
import com.employee.employeeandworkordermanagement.repository.BreakTimeRepository;
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
    private final UserService userService;

    public Duration workingDurationWithBreaks(List<BreakTime> breakTimeList, WorkingSession workingSession) {
        List<BreakTime> filteredList = breakTimeList.stream().filter(b -> b.getStartTime().isAfter(
                workingSession.getCreatedAt())).toList();
        Duration totalBreaksDuration = filteredList.stream()
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
        userService.checkCurrentDesigner(task, authentication);
        BreakTime breakTime = new BreakTime();
        breakTime.setStartTime(Instant.now());
        breakTime.setUser(task.getDesigner());
        breakTime.setWorkingAtTaskName(task.getTaskName());
        breakTimeRepository.save(breakTime);
    }

    public void stopBreakTime(Task task, Authentication authentication) {
        userService.checkCurrentDesigner(task, authentication);
        List<BreakTime> breakTimeList = task.getDesigner().getBreakTimes();
        BreakTime breakTime = breakTimeList.stream().filter(BreakTime::isActive).findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Breaking time has not been found."));
        breakTime.setFinishTime(Instant.now());
        breakTime.setActive(false);
        breakTimeRepository.save(breakTime);
    }

    public boolean showStopButton(Task task) {
        List<BreakTime> breakTimeList = task.getDesigner().getBreakTimes();
        return breakTimeList.stream().anyMatch(BreakTime::isActive);
    }

    public boolean checkIfBreakIsActive(User user) {
        List<BreakTime> breakTimeList = user.getBreakTimes();
        return breakTimeList.stream().anyMatch(BreakTime::isActive);
    }
}

package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.entity.WorkingSession;
import com.employee.employeeandworkordermanagement.repository.WorkingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingSessionService {
    private final WorkingSessionRepository workingSessionRepository;
    private final UserService userService;

    public WorkingSession findById(Long id) {
        return workingSessionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Working time has not been found."));
    }

//    public void startWorking(WorkingTime workingTime) {
//        if (workingTime.getTask().getTaskStatus() == TaskStatus.CLOSED) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, workingTime.getTask().getTaskName() + " is closed.");
//        }
//        workingTime.setWorkStarted(Instant.now());
//        workingTimeRepository.save(workingTime);
//    }

    public void stopWorking(WorkingSession workingSession) {
        workingSession.setWorkFinished(Instant.now());
        workingSessionRepository.save(workingSession);
    }

    public void createWorkDay(User user) {
        List<WorkingSession> workingSessions = workingSessionRepository.findAllByUser(user);
        boolean isWorkingDayExist = workingSessions.stream()
                .anyMatch(day -> isSameDay(day.getCreatedAt(), Instant.now()));
        if (isWorkingDayExist) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This working day already exists");
        } else {
            WorkingSession workingSession = new WorkingSession();
            workingSession.setUser(user);
            workingSession.setCreatedAt(Instant.now());
            workingSessionRepository.save(workingSession);
        }
    }

    public Duration calculateTotalWorkingTime(WorkingSession workingSession) {
        if (workingSession.getWorkStarted() == null || workingSession.getWorkFinished() == null) {
            return Duration.ZERO;
        }
        return Duration.between(workingSession.getWorkStarted(), workingSession.getWorkFinished());
    }

    private boolean isSameDay(Instant instant1, Instant instant2) {
        LocalDate date1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault()).toLocalDate();
        return date1.equals(date2);
    }

    public Page<WorkingSession> getUserSortedWorkingTimePage(int page, String direction, String sortField, User user) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, 50, sort);
        return workingSessionRepository.findAllByUser(user, pageable);
    }

    public Page<WorkingSession> getAllSortedWorkingTimePage(int page, String direction, String sortField) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, 50, sort);
        return workingSessionRepository.findAll(pageable);
    }

//    public void initializeWorkingTime(Task task) {
//        WorkingTime workingTime = new WorkingTime();
//        workingTime.setTask(task);
//        workingTime.setUser(task.getDesigner());
//        workingTime.setCreatedAt(Instant.now());
//        workingTimeRepository.save(workingTime);
//    }
}

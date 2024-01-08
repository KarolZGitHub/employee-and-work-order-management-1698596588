package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.WorkingTime;
import com.employee.employeeandworkordermanagement.repository.WorkingTimeRepository;
import com.employee.employeeandworkordermanagement.entity.User;
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
public class WorkingTimeService {
    private final WorkingTimeRepository workingTimeRepository;
    private final UserService userService;

    public WorkingTime findById(Long id) {
        return workingTimeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Working time has not been found."));
    }

    public void startWorking(WorkingTime workingTime) {
        if (workingTime.getTask().getTaskStatus() == TaskStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, workingTime.getTask().getTaskName() + " is closed.");
        }
        workingTime.setWorkStarted(Instant.now());
        workingTimeRepository.save(workingTime);
    }

    public void stopWorking(WorkingTime workingTime) {
        workingTime.setWorkFinished(Instant.now());
        workingTimeRepository.save(workingTime);
    }

    public void createWorkDay(User user) {
        List<WorkingTime> workingTimes = workingTimeRepository.findAllByUser(user);
        boolean isWorkingDayExist = workingTimes.stream()
                .anyMatch(day -> isSameDay(day.getCreatedAt(), Instant.now()));
        if (isWorkingDayExist) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This working day already exists");
        } else {
            WorkingTime workingTime = new WorkingTime();
            workingTime.setUser(user);
            workingTime.setCreatedAt(Instant.now());
            workingTimeRepository.save(workingTime);
        }
    }

    public Duration calculateTotalWorkingTime(WorkingTime workingTime) {
        if (workingTime.getWorkStarted() == null || workingTime.getWorkFinished() == null) {
            return Duration.ZERO;
        }
        return Duration.between(workingTime.getWorkStarted(), workingTime.getWorkFinished());
    }

    private boolean isSameDay(Instant instant1, Instant instant2) {
        LocalDate date1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault()).toLocalDate();
        return date1.equals(date2);
    }

    public Page<WorkingTime> getUserSortedWorkingTimePage(int page, String direction, String sortField, User user) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, 50, sort);
        return workingTimeRepository.findAllByUser(user, pageable);
    }

    public Page<WorkingTime> getAllSortedWorkingTimePage(int page, String direction, String sortField) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, 50, sort);
        return workingTimeRepository.findAll(pageable);
    }

//    public void initializeWorkingTime(Task task) {
//        WorkingTime workingTime = new WorkingTime();
//        workingTime.setTask(task);
//        workingTime.setUser(task.getDesigner());
//        workingTime.setCreatedAt(Instant.now());
//        workingTimeRepository.save(workingTime);
//    }
}

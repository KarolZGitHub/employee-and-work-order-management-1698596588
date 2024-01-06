package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.WorkingTime;
import com.employee.employeeandworkordermanagement.repository.WorkingTimeRepository;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingTimeService {
    private final WorkingTimeRepository workingTimeRepository;
    private final UserService userService;

    public WorkingTime findById(Long id) {
        return workingTimeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Working day has not been found."));
    }

    public void startWorking(WorkingTime workingTime, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        List<WorkingTime> workingTimeList = workingTimeRepository.findAllByTheUser(user);
        if (workingTimeList.stream().anyMatch(WorkingTime::isWorking)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "There cannot be more than one work in progress");
        }
        if (workingTime.getTask().getTaskStatus() == TaskStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, workingTime.getTask().getTaskName() + " is closed.");
        }
        if(workingTime.isWorking()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This is already active.");
        }
        workingTime.setCurrentWorkingTime(workingTime.getOverallWorkingTime());
        workingTime.setWorking(true);
        workingTimeRepository.save(workingTime);

    }

    public void stopWorking(WorkingTime workingTime) {
        if(!workingTime.isWorking()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This is already inactive.");
        }
        workingTime.setOverallWorkingTime(new Date().getTime() - workingTime.getWorkStarted().getTime());
        workingTime.setWorking(false);
        workingTimeRepository.save(workingTime);
    }

    public void createWorkDay(User user) {
        List<WorkingTime> workingTimes = workingTimeRepository.findAllByTheUser(user);
        boolean isWorkingDayExist = workingTimes.stream()
                .anyMatch(day -> isSameDay(day.getCreatedAt(), new Date()));
        if (isWorkingDayExist) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This working day already exists");
        } else {
            WorkingTime workingTime = new WorkingTime();
            workingTime.setTheUser(user);
            workingTime.setCreatedAt(new Date());
            workingTime.setCurrentWorkingTime(0L);
            workingTime.setOverallWorkingTime(0L);
            workingTime.setTheUser(user);
            workingTime.setWorking(false);
            workingTimeRepository.save(workingTime);
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public Page<WorkingTime> getUserSortedWorkingTimePage(int page, String direction, String sortField, User user) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, 50, sort);
        return workingTimeRepository.findAllByTheUser(user,pageable);
    }
    public Page<WorkingTime> getAllSortedWorkingTimePage(int page, String direction, String sortField) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, 50, sort);
        return workingTimeRepository.findAll(pageable);
    }

    public void updateWorkingFunction(WorkingTime workingTime) {
        long duration = new Date().getTime() - workingTime.getWorkStarted().getTime();
        workingTime.setCurrentWorkingTime(duration);
        workingTimeRepository.save(workingTime);
    }

    public void updateWorkingTime(List<WorkingTime> workingTimeList) {
        workingTimeList.stream().filter(workingTime -> workingTime.isWorking()).forEach((workingTime -> updateWorkingFunction(workingTime)));
    }

    public List<WorkingTime> findAll() {
        return workingTimeRepository.findAll();
    }
}

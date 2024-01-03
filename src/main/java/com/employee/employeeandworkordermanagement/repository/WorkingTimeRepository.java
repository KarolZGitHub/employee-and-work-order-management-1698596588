package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.entity.WorkingTime;
import com.employee.employeeandworkordermanagement.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkingTimeRepository extends JpaRepository<WorkingTime, Long> {
    List<WorkingTime> findAllByTheUser(User user);
}

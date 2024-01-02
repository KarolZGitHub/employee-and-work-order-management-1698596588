package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.entity.WorkingTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingTimeRepository extends JpaRepository<WorkingTime, Long> {
}

package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.entity.WorkingDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingDurationRepository extends JpaRepository<WorkingDuration,Long> {
}

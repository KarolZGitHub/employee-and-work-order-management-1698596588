package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.entity.BreakTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreakTimeRepository extends JpaRepository<BreakTime,Long> {
}

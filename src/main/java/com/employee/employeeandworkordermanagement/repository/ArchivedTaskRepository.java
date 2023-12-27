package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.entity.ArchivedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedTaskRepository extends JpaRepository<ArchivedTask, Long> {
}

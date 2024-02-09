package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.entity.TaskFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskFeedbackRepository extends JpaRepository<TaskFeedback,Long> {
}

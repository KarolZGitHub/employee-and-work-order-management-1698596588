package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);

    Page<Task> findAllByTaskStatus(TaskStatus taskStatus, Pageable pageable);
    Page<Task> findAllByTaskStatusNot(TaskStatus taskStatus,Pageable pageable);
}

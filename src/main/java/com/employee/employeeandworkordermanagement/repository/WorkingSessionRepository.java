package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.entity.WorkingSession;
import com.employee.employeeandworkordermanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WorkingSessionRepository extends JpaRepository<WorkingSession, Long> {
    List<WorkingSession> findAllByUser(User user);
    Page<WorkingSession> findAllByUser(User user, Pageable pageable);
}

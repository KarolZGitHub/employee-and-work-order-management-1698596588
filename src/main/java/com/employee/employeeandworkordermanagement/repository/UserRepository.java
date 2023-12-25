package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(long id);

    List<User> findByRole(Role role);

    Page<User> findByRole(Role role, Pageable pageable);
}

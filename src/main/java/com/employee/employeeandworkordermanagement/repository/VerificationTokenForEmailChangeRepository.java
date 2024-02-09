package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.registration.token.VerificationTokenForEmailChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VerificationTokenForEmailChangeRepository extends JpaRepository<VerificationTokenForEmailChange, Long> {
    Optional<VerificationTokenForEmailChange> findByToken(String token);

    Optional<VerificationTokenForEmailChange> findById(Long id);
}

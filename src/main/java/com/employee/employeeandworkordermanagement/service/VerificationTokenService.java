package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.registration.token.VerificationToken;
import com.employee.employeeandworkordermanagement.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken getToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token doesn't exist"));
        return verificationToken;
    }
}

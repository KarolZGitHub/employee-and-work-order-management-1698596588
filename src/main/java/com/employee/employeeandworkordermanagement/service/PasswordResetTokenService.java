package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.password.PasswordResetToken;
import com.employee.employeeandworkordermanagement.repository.PasswordResetTokenRepository;
import com.employee.employeeandworkordermanagement.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;


@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public PasswordResetToken getToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token doesn't exist"));
        return passwordResetToken;
    }

    public String validatePasswordResetToken(String theToken) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(theToken).orElse(
                new PasswordResetToken());
        Calendar calendar = Calendar.getInstance();
        if (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "token expired";
        }
        return "valid";
    }
}


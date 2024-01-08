package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.registration.token.VerificationTokenForEmailChange;
import com.employee.employeeandworkordermanagement.repository.VerificationTokenForEmailChangeRepository;
import com.employee.employeeandworkordermanagement.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangeEmailResetTokenService {
    private final VerificationTokenForEmailChangeRepository verificationTokenForEmailChangeRepository;

    public void createEmailChangeTokenForUser(User user, String emailToken) {
        VerificationTokenForEmailChange verificationTokenForEmailChange = new VerificationTokenForEmailChange(emailToken, user);
        Optional<VerificationTokenForEmailChange> presentEmailChangeToken = verificationTokenForEmailChangeRepository.findById(1L);
        presentEmailChangeToken.ifPresent(verificationTokenForEmailChangeRepository::delete);
        verificationTokenForEmailChangeRepository.save(verificationTokenForEmailChange);
    }

    public String validatePasswordResetToken(String theToken) {
        VerificationTokenForEmailChange verificationTokenForEmailChange = verificationTokenForEmailChangeRepository.findByToken(theToken).orElse(
                new VerificationTokenForEmailChange());
        Calendar calendar = Calendar.getInstance();
        if (verificationTokenForEmailChange.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenForEmailChangeRepository.delete(verificationTokenForEmailChange);
            return "token expired";
        }
        return "valid";
    }
}

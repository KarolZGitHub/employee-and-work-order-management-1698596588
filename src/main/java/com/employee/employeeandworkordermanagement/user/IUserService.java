package com.employee.employeeandworkordermanagement.user;

import com.employee.employeeandworkordermanagement.Registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.Registration.token.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);

    String validateToken(String theToken);

    void createPasswordResetTokenForUser(User user, String passwordToken);
}

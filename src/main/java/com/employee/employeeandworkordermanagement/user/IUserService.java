package com.employee.employeeandworkordermanagement.user;

import com.employee.employeeandworkordermanagement.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();

    User registerUser(RegistrationRequest request);

    Optional<User> findByEmail(String email);

    String validateToken(String theToken);
    String validateResetPasswordToken(String theToken);
    void saveUser(User user,String password, String repeatedPassword);

    void createPasswordResetTokenForUser(User user, String passwordToken);
    String changePasswordProcess(User user, String password, String repeatPassword);
}

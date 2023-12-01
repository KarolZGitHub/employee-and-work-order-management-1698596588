package com.employee.employeeandworkordermanagement.user;

import com.employee.employeeandworkordermanagement.Registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);
}

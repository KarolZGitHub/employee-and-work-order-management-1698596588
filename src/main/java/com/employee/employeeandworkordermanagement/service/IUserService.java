package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface IUserService {
    User registerUser(RegistrationRequest request);

    Optional<User> findByEmail(String email);

    String validateToken(String theToken);

    String validateResetPasswordToken(String theToken);

    void saveUser(User user, String password, String repeatedPassword);

    void createPasswordResetTokenForUser(User user, String passwordToken);

    String changePasswordProcess(User user, String password, String repeatPassword);

    Page<User> getAllUsers(PageRequest pageRequest);

    User findById(Long id);

    void editUserRole(User user, Role role);

    UserDTO convertUserToUserDTO(User user);

    UserDTO getUser(Authentication authentication);
    boolean changePassword(User user, String password, String repeatPassword);
}

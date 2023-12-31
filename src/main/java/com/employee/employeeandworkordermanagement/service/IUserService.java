package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User registerUser(RegistrationRequest request);

    Optional<User> findByEmail(String email);

    String validateToken(String theToken);

    void saveUser(User user, String password, String repeatedPassword);

    void createPasswordResetTokenForUser(User user, String passwordToken);

    Page<User> getAllUsers(PageRequest pageRequest);

    User findById(Long id);

    void editUserRole(User user, Role role);

    UserDTO convertUserToUserDTO(User user);

    UserDTO getUserDTO(Authentication authentication);

    boolean changePassword(User user, String password, String repeatPassword);

    void changeFirstName(User user, String firstName);

    void changeLastName(User user, String lastName);

    void saveEmailForUser(User user, String email);

    List<User> getDesigners();

    Page<User> designerPage(PageRequest pageRequest);
}

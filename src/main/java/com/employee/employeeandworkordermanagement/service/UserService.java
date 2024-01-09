package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.exception.UserAlreadyExistsException;
import com.employee.employeeandworkordermanagement.registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.registration.token.VerificationToken;
import com.employee.employeeandworkordermanagement.repository.UserRepository;
import com.employee.employeeandworkordermanagement.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;


    public Page<User> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }


    public User registerUser(RegistrationRequest request) {
        Optional<User> user = this.findOptionalUserByEmail(request.email());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists.", "USER_ALREADY_EXISTS");
        }
        var newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setRole(Role.DESIGNER);
        newUser.setPassword(passwordEncoder.encode(request.password()));
        return userRepository.save(newUser);
    }


    public Optional<User> findOptionalUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"User has not been found."));
    }

    public void saveUserVerificationToken(User theUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }


    public String validateToken(String theToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(theToken).orElse(
                new VerificationToken());
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "token expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }


    public void saveUser(User user, String password, String repeatedPassword) {
        if (password.equals(repeatedPassword)) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }


    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
    }


    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User of this ID has not been found"));
        return user;
    }


    public void editUserRole(User user, Role role) {
        if (user != null && userRepository.existsById(Long.valueOf(user.getId()))) {
            user.setRole(role);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found or invalid user object");
        }
    }


    public UserDTO convertUserToUserDTO(User user) {
        return new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.getProfilePicturePath());
    }


    public UserDTO getUserDTO(Authentication authentication) {
        User user = findOptionalUserByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        return convertUserToUserDTO(user);
    }


    public boolean changePassword(User user, String password, String repeatPassword) {
        if (password.equals(repeatPassword)) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }


    public void changeFirstName(User user, String firstName) {
        user.setFirstName(firstName);
        userRepository.save(user);
    }


    public void changeLastName(User user, String lastName) {
        user.setLastName(lastName);
        userRepository.save(user);
    }


    public void saveEmailForUser(User user, String email) {
        user.setEmail(email);
        userRepository.save(user);
    }


    public List<User> getAvailableDesigners() {
        return userRepository.findByAssignedTaskIsNull();
    }


    public Page<User> designerPage(PageRequest pageRequest) {
        return userRepository.findByRole(Role.DESIGNER, pageRequest);
    }

}

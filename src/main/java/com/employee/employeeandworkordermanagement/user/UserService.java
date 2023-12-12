package com.employee.employeeandworkordermanagement.user;

import com.employee.employeeandworkordermanagement.data.Role;
import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.exception.UserAlreadyExistsException;
import com.employee.employeeandworkordermanagement.password.PasswordResetToken;
import com.employee.employeeandworkordermanagement.password.PasswordResetTokenRepository;
import com.employee.employeeandworkordermanagement.password.PasswordResetTokenService;
import com.employee.employeeandworkordermanagement.registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.registration.token.VerificationToken;
import com.employee.employeeandworkordermanagement.registration.token.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public Page<User> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = this.findByEmail(request.email());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists.", "USER_ALREADY_EXISTS");
        }
        var newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setRole(Role.USER);
        newUser.setPassword(passwordEncoder.encode(request.password()));
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUserVerificationToken(User theUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
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

    @Override
    public String validateResetPasswordToken(String theToken) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(theToken).orElse(
                new PasswordResetToken());
        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "token expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public void saveUser(User user, String password, String repeatedPassword) {
        if (password.equals(repeatedPassword)) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
    }

    @Override
    public String changePasswordProcess(User user, String password, String repeatPassword) {
        User theUser = user;
        if (password.equalsIgnoreCase(repeatPassword)) {
            theUser.setPassword(password);
            userRepository.save(theUser);
            return "Success";
        }
        return "Passwords are different";
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User of this ID has not been found"));
        return user;
    }

    @Override
    public void editUserRole(User user, Role role) {
        if (user != null && userRepository.existsById(Long.valueOf(user.getId()))) {
            user.setRole(role);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found or invalid user object");
        }
    }

    @Override
    public UserDTO convertUserToUserDTO(User user) {
        return new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.getProfilePicturePath());
    }

    @Override
    public UserDTO getUser(Authentication authentication) {
        User user = findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        return convertUserToUserDTO(user);
    }
}

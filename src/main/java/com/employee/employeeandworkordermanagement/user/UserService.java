package com.employee.employeeandworkordermanagement.user;

import com.employee.employeeandworkordermanagement.Registration.RegistrationRequest;
import com.employee.employeeandworkordermanagement.Registration.token.VerificationToken;
import com.employee.employeeandworkordermanagement.Registration.token.VerificationTokenRepository;
import com.employee.employeeandworkordermanagement.exception.UserAlreadyExistsException;
import com.employee.employeeandworkordermanagement.password.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
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
        newUser.setRole("USER");
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
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
    }
}

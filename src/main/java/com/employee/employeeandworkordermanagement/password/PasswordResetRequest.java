package com.employee.employeeandworkordermanagement.password;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    @Email
    private String email;
}

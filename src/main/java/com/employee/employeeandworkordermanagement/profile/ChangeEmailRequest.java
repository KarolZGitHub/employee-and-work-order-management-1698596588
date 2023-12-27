package com.employee.employeeandworkordermanagement.profile;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeEmailRequest {
    @Email
    private String email;
    @Email
    private String confirmEmail;
}

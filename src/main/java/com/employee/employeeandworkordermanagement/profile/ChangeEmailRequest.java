package com.employee.employeeandworkordermanagement.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeEmailRequest {
    private String email;
    private String confirmEmail;
}

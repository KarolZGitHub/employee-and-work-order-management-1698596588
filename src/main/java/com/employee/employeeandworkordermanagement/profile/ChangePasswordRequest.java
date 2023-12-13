package com.employee.employeeandworkordermanagement.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String password;
    private String repeatPassword;
}
